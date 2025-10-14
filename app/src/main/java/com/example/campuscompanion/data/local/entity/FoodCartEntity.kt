package com.example.campuscompanion.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "food_cart",
    primaryKeys = ["userId", "cafeteriaId", "foodId"]
)
data class FoodCartEntity(
    val userId: String,
    val cafeteriaId: String,
    val foodId: String,
    val foodName: String,
    val price: Int,
    val imageUrl: String,
    val quantity: Int
)
