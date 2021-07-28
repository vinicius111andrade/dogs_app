package com.vdemelo.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vdemelo.dogs.model.DogBreed
import com.vdemelo.dogs.model.DogDataBase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int) {
        launch {
            val dog = DogDataBase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value = dog
        }
    }
}

