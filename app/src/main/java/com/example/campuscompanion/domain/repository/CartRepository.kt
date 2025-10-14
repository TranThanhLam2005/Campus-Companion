package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Food
import com.example.campuscompanion.domain.model.FoodOrder
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(userId: String, cafeteriaId: String): Flow<List<FoodOrder>>
    suspend fun addOrIncrement(userId: String, cafeteriaId: String, food: Food, delta: Int)
    suspend fun setQuantity(userId: String, cafeteriaId: String, foodId: String, quantity: Int)
    suspend fun clearCart(userId: String, cafeteriaId: String)
}
