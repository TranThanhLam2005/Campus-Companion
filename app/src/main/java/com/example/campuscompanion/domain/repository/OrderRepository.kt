package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Order

interface OrderRepository {
    suspend fun addOrder(order: Order): String
    suspend fun getOrderDetail(orderId: String): Order
    suspend fun getOrdersByUserId(userId: String, status: String): List<Order>
    suspend fun updateStatus(orderId: String, newStatus: String)
}