package com.example.favdish.model.database

import androidx.annotation.WorkerThread
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFavDishDetails(favDish)
    }

    val favDishesList: Flow<List<FavDish>> = favDishDao.getFavDishesList()


}