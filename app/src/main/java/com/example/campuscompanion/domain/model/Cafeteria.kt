package com.example.campuscompanion.domain.model

data class Cafeteria(
    val id: String = "",
    val name: String = "",
    val title: String = "",
    val description: String = "",
    val star: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val foodTypeList: List<FoodType> = emptyList()
)
data class Food(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
)
data class FoodType(
    val id: String = "",
    val name: String = "",
    val foodList: List<Food> = emptyList()
)