package com.vdemelo.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vdemelo.dogs.model.DogBreed
import com.vdemelo.dogs.model.DogDataBase
import com.vdemelo.dogs.model.DogsApiService
import com.vdemelo.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())

    // correct name: timeToLive
    private var refreshTime: Long =
        5 * 60 * 1000 * 1000 * 1000L //5 min em nano sec, o L eh pra salvar em Long

    private val dogService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefHelper.getUpdateTime()

        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else
            fetchFromRemote()
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDataBase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)

            Toast.makeText(
                getApplication(),
                "Dogs retrieved from database",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)

                        Toast.makeText(
                            getApplication(),
                            "Dogs retrieved from endpoint",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(err: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        err.printStackTrace()
                    }
                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDataBase(getApplication()).dogDao()

            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())

            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                i++
            }

            dogsRetrieved(list)
        }

        prefHelper.saveUpdateTime(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
