package com.example.campuscompanion.presentation.feature.orderhistoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.usecase.GetOrdersUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {
    private val _isLoading =  MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _orders.value = userId?.let { getOrdersUseCase(it) }!!
            _isLoading.value = false
        }
    }
}