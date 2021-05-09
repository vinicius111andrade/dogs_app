package com.vdemelo.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vdemelo.dogs.model.DogBreed

class DetailViewModel : ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch() {
        val dog = DogBreed("1", "Corgi", "15 years", "breedGroup", "bredFor", "temp", "")
        dogLiveData.value = dog
    }
}

