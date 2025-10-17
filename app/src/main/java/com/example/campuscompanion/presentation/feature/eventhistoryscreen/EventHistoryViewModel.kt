package com.example.campuscompanion.presentation.feature.eventhistoryscreen

import androidx.lifecycle.ViewModel
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.usecase.GetParticipantEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EventHistoryViewModel @Inject constructor(
    private val getParticipantEventsUseCase: GetParticipantEventsUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    suspend fun loadParticipatedEvents() {
        _isLoading.value = true
        try {
            val participatedEvents = getParticipantEventsUseCase()
            _events.value = participatedEvents
        } finally {
            _isLoading.value = false
        }
    }

}