package com.example.campuscompanion.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.campuscompanion.data.local.dao.CartDao
import com.example.campuscompanion.data.local.dao.OrderDao
import com.example.campuscompanion.data.local.entity.FoodCartEntity
import com.example.campuscompanion.data.local.entity.OrderEntity

@Database(entities = [OrderEntity::class, FoodCartEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun cartDao(): CartDao
}