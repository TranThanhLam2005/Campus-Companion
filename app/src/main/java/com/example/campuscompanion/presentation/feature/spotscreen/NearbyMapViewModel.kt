package com.example.campuscompanion.presentation.feature.spotscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Place
import com.example.campuscompanion.domain.usecase.GetNearbyPlaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyMapViewModel @Inject constructor(
    private val getNearbyPlaceUseCase: GetNearbyPlaceUseCase

): ViewModel() {
    private val _nearbyPlaces = MutableStateFlow<List<Place>>(emptyList())
    val nearbyPlaces = _nearbyPlaces.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun loadNearbyPlaces() {
        viewModelScope.launch {
            if (_nearbyPlaces.value.isNotEmpty()) return@launch
            _isLoading.value = true
            _nearbyPlaces.value = getNearbyPlaceUseCase()
            _isLoading.value = false
        }
    }
}