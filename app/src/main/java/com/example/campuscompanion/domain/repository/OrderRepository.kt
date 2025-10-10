package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Order

interface OrderRepository {
    suspend fun addOrder(order: Order): String
    suspend fun getOrdersByUserId(userId: String): List<Order>
}