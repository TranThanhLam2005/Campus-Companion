package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.repository.OrderRepository
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order): String {
        return orderRepository.addOrder(order)
    }
}