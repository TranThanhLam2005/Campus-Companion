package com.example.campuscompanion.presentation.feature.spotscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.model.Food
import com.example.campuscompanion.domain.model.FoodOrder
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.repository.CartRepository
import com.example.campuscompanion.domain.usecase.AddOrderUseCase
import com.example.campuscompanion.domain.usecase.GetCafeteriaUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeteriaDetailViewModel @Inject constructor(
    private val getCafeteriaUseCase: GetCafeteriaUseCase,
    private val addOrderUseCase: AddOrderUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cafeteria = MutableStateFlow<Cafeteria?>(null)
    val cafeteria = _cafeteria.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _lastOrderId = MutableStateFlow("")
    val lastOrderId = _lastOrderId.asStateFlow()

    private val _cartItems = MutableStateFlow<List<FoodOrder>>(emptyList())
    val cartItems: StateFlow<List<FoodOrder>> = _cartItems

    private val auth = FirebaseAuth.getInstance()
    private var cartObserveJob: Job? = null

    fun loadCafeteria(cafeteriaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _cafeteria.value = getCafeteriaUseCase(cafeteriaId)
                observeCart(cafeteriaId)
            } catch (e: Exception) {
                Log.e("CafeteriaViewModel", "❌ Failed to load cafeteria: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun userIdOrNull(): String? = auth.currentUser?.uid

    private fun observeCart(cafeteriaId: String) {
        val uid = userIdOrNull() ?: return
        cartObserveJob?.cancel()
        cartObserveJob = viewModelScope.launch {
            cartRepository.observeCart(uid, cafeteriaId).collect { list ->
                _cartItems.value = list
            }
        }
    }

    fun addToCart(cafeteriaId: String, food: Food) {
        val uid = userIdOrNull() ?: return
        viewModelScope.launch { cartRepository.addOrIncrement(uid, cafeteriaId, food, 1) }
    }

    fun updateQuantity(cafeteriaId: String, foodId: String, delta: Int) {
        val uid = userIdOrNull() ?: return
        val currentQty = _cartItems.value.firstOrNull { it.food.id == foodId }?.quantity ?: 0
        val newQty = currentQty + delta
        viewModelScope.launch {
            if (newQty <= 0) {
                cartRepository.setQuantity(uid, cafeteriaId, foodId, 0)
            } else {
                cartRepository.setQuantity(uid, cafeteriaId, foodId, newQty)
            }
        }
    }

    fun clearCart(cafeteriaId: String) {
        val uid = userIdOrNull() ?: return
        viewModelScope.launch { cartRepository.clearCart(uid, cafeteriaId) }
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

                val orderId = addOrderUseCase(order)

                _lastOrderId.value = orderId
                clearCart(cafeteriaId)
                Log.d("OrderViewModel", "✅ Order created with ID: $orderId")

            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error adding order: ${e.message}")
            }
        }
    }
}
