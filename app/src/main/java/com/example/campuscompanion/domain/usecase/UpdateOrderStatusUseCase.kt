package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, newStatus: String) {
        orderRepository.updateStatus(orderId, newStatus)
    }
}