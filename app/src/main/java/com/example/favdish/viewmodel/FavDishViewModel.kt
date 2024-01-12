package com.example.favdish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.favdish.model.database.FavDishRepository
import com.example.favdish.model.entities.FavDish
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class FavDishViewModel(private val repository: FavDishRepository) : ViewModel() {

    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }

    class FavDishViewModelFactory(private val repository: FavDishRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
                FavDishViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
