package com.example.favdish.utils

object Constants {
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING: String = "DishCooking"

    fun dishTypes(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Breakfast")
        list.add("Lunch")
        list.add("Snacks")
        list.add("Dinner")
        list.add("Other")

        return list
    }

    fun dishCategories(): ArrayList<String>{
        val categoriesList = ArrayList<String>()
        categoriesList.add("Pizza")
        categoriesList.add("Burger")
        categoriesList.add("Bakery")
        categoriesList.add("Chicken")
        categoriesList.add("Dessert")
        categoriesList.add("Sandwich")
        categoriesList.add("Wrap")
        categoriesList.add("Juices")
        categoriesList.add("Hot Dog")
        categoriesList.add("Tea and Coffee")
        return categoriesList
    }

    fun dishCookingTime(): ArrayList<String>{
        val cookingTimeList = ArrayList<String>()
        cookingTimeList.add("10")
        cookingTimeList.add("15")
        cookingTimeList.add("20")
        cookingTimeList.add("25")
        cookingTimeList.add("30")
        cookingTimeList.add("35")
        cookingTimeList.add("50")
        return cookingTimeList
    }
}