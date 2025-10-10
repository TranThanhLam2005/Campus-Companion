package com.example.campuscompanion.presentation.feature.spotscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.model.FoodOrder
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.usecase.AddOrderUseCase
import com.example.campuscompanion.domain.usecase.GetCafeteriaUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeteriaDetailViewModel @Inject constructor(
    private val getCafeteriaUseCase: GetCafeteriaUseCase,
    private val addOrderUseCase: AddOrderUseCase
) : ViewModel() {

    private val _cafeteria = MutableStateFlow<Cafeteria?>(null)
    val cafeteria = _cafeteria.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _lastOrderId = MutableStateFlow("")
    val lastOrderId = _lastOrderId.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    fun loadCafeteria(cafeteriaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _cafeteria.value = getCafeteriaUseCase(cafeteriaId)
            } catch (e: Exception) {
                Log.e("CafeteriaViewModel", "❌ Failed to load cafeteria: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addOrder(
        cafeteriaId: String,
        cartItems: List<FoodOrder>,
        status: String,
        total: Int,
        note: String,
        name: String
    ) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")

                val order = Order(
                    userId = userId,
                    cafeteriaId = cafeteriaId,
                    foodOrderList = cartItems,
                    status = status,
                    totalPrice = total,
                    note = note,
                    name = name
                )

                // ✅ Use your AddOrderUseCase (which returns the orderId)
                val orderId = addOrderUseCase(order)

                _lastOrderId.value = orderId
                Log.d("OrderViewModel", "✅ Order created with ID: $orderId")

            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error adding order: ${e.message}")
            }
        }
    }
}
