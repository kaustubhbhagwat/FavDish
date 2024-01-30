package com.example.favdish.utils

object Constants {
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING: String = "DishCooking"

    const val DISH_IMAGE_SOURCE_LOCAL: String = "LOCAL"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "ONLINE"

    const val API_ENDPOINT: String = "recipes/random"

    const val API_KEY: String = "apiKey"
    const val LIMIT_LICENSE: String = "limitLicense"
    const val TAGS: String = "tags"
    const val NUMBER: String = "number"

    const val BASE_URL: String = "https://api.spoonacular.com/"

    const val API_KEY_VALUE: String = "f5e06fb150a345afb5172a09809cd3c9"
    const val LIMIT_LICENSE_VALUE: Boolean = true
    const val TAGS_VALUE: String = "vegetarian"
    const val NUMBER_VALUE: Int = 1

    const val NOTIFICATION_ID: String = "FavDish_notificationId"
    const val NOTIFICATION_NAME: String = "FavDish"
    const val NOTIFICATION_CHANNEL: String = "FavDish_channel_1"





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