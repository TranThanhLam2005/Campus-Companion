package com.example.campuscompanion.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.campuscompanion.data.local.entity.FoodCartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert
    suspend fun upsert(item: FoodCartEntity)

    @Query("SELECT * FROM food_cart WHERE userId = :userId AND cafeteriaId = :cafeteriaId ORDER BY foodName")
    fun observeCart(userId: String, cafeteriaId: String): Flow<List<FoodCartEntity>>

    @Query("SELECT * FROM food_cart WHERE userId = :userId AND cafeteriaId = :cafeteriaId")
    suspend fun getCart(userId: String, cafeteriaId: String): List<FoodCartEntity>

    @Query("SELECT quantity FROM food_cart WHERE userId = :userId AND cafeteriaId = :cafeteriaId AND foodId = :foodId LIMIT 1")
    suspend fun getQuantity(userId: String, cafeteriaId: String, foodId: String): Int?

    @Query("UPDATE food_cart SET quantity = :quantity WHERE userId = :userId AND cafeteriaId = :cafeteriaId AND foodId = :foodId")
    suspend fun updateQuantity(userId: String, cafeteriaId: String, foodId: String, quantity: Int)

    @Query("DELETE FROM food_cart WHERE userId = :userId AND cafeteriaId = :cafeteriaId AND foodId = :foodId")
    suspend fun deleteItem(userId: String, cafeteriaId: String, foodId: String)

    @Query("DELETE FROM food_cart WHERE userId = :userId AND cafeteriaId = :cafeteriaId")
    suspend fun clearCart(userId: String, cafeteriaId: String)
}
