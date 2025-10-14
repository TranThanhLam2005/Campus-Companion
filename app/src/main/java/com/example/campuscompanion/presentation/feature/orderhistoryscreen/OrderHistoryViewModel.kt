package com.example.campuscompanion.presentation.feature.orderhistoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.usecase.GetCafeteriaUseCase
import com.example.campuscompanion.domain.usecase.GetOrdersUseCase
import com.example.campuscompanion.domain.usecase.UpdateOrderStatusUseCase
import com.example.campuscompanion.domain.usecase.AddOrderUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCafeteriaUseCase: GetCafeteriaUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val addOrderUseCase: AddOrderUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _ordersWithCafeteria = MutableStateFlow<List<OrderWithCafeteria>>(emptyList())
    val ordersWithCafeteria = _ordersWithCafeteria.asStateFlow()

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun loadOrders(status: String) {
        viewModelScope.launch {
            _isLoading.value = true

            userId?.let { uid ->
                val orders = getOrdersUseCase(uid, status)

                // Fetch cafeteria info for each order
                val enrichedOrders = orders.map { order ->
                    val cafeteria = getCafeteriaUseCase(order.cafeteriaId)
                    OrderWithCafeteria(order, cafeteria)
                }

                _ordersWithCafeteria.value = enrichedOrders
            }

            _isLoading.value = false
        }
    }
    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            _isLoading.value = true
            updateOrderStatusUseCase(orderId, newStatus)
            _isLoading.value = false
        }
    }

    fun reorder(oldOrder: Order, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newOrder = oldOrder.copy(
                    id = "", // let repository generate a new ID
                    status = OrderStatus.PENDING.code,
                    orderedAt = null // repository will set Timestamp.now()
                )
                val newId = addOrderUseCase(newOrder)
                onSuccess(newId)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class OrderWithCafeteria(
    val order: Order,
    val cafeteria: Cafeteria? = null
)