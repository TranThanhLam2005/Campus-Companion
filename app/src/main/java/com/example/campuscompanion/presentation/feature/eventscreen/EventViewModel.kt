package com.example.campuscompanion.presentation.feature.eventscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.usecase.GetEventDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEventDetailUseCase: GetEventDetailUseCase
): ViewModel() {

    private val _eventDetail = MutableStateFlow<Event?>(null)
    val eventDetail = _eventDetail
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    suspend fun loadEventDetail(clubId: String, eventId: String) {
        viewModelScope.launch {
            if (_eventDetail.value != null) return@launch
            _isLoading.value = true
            try {
                _eventDetail.value = getEventDetailUseCase(clubId, eventId)
            } catch (e: Exception) {
                // Handle error (e.g., log it, show a message to the user)
            } finally {
                _isLoading.value = false
            }
        }
    }
}