package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(userId: String): List<Order> {
        return orderRepository.getOrdersByUserId(userId)
    }
}