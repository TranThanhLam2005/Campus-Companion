package com.example.campuscompanion.presentation.feature.orderhistoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.campuscompanion.presentation.feature.orderhistoryscreen.OrderStatus
import com.example.campuscompanion.domain.usecase.AddOrderUseCase

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val addOrderUseCase: AddOrderUseCase
) : ViewModel() {
    private val _order = MutableStateFlow<Order?>(null)
    val order = _order.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadOrder(orderId: String){
        viewModelScope.launch {
            _isLoading.value = true
            _order.value = orderRepository.getOrderDetail(orderId)
            _isLoading.value = false
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            orderRepository.updateStatus(orderId, OrderStatus.CANCELLED.code)
            _order.value = orderRepository.getOrderDetail(orderId)
            _isLoading.value = false
        }
    }

    fun reorder(current: Order, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newOrder = current.copy(
                    id = "",
                    status = OrderStatus.PENDING.code,
                    orderedAt = null
                )
                val newId = addOrderUseCase(newOrder)
                onSuccess(newId)
            } finally {
                _isLoading.value = false
            }
        }
    }
}