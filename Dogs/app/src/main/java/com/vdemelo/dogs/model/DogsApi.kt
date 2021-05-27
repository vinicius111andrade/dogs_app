package com.vdemelo.dogs.model

import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created on 5/27/2021
 * @author Vinicius de Melo Andrade
 */
interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}