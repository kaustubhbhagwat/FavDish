package com.example.favdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.favdish.model.entities.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISH_TABLE ORDER BY ID")
    fun getAllDishesList(): kotlinx.coroutines.flow.Flow<List<FavDish>>

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Query("Select * from FAV_DISH_TABLE WHERE favourite_dish = 1")
    fun getFavDishesList() :kotlinx.coroutines.flow.Flow<List<FavDish>>
}