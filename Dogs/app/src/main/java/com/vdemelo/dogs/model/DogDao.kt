package com.vdemelo.dogs.model

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>
}