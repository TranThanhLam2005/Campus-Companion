package com.example.campuscompanion.domain.model

import com.google.firebase.Timestamp

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

data class FoodOrder(
    val food: Food = Food(),
    var quantity: Int = 0
)

data class Order(
    val id: String = "",
    val userId: String = "",
    val cafeteriaId: String = "",
    val foodOrderList: List<FoodOrder> = emptyList(),
    val status: String = "",
    val totalPrice: Int = 0,
    val note: String = "",
    val name: String = "",
    val orderedAt: Timestamp? = null,
)