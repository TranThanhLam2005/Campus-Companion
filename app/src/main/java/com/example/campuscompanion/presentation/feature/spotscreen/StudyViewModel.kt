package com.example.campuscompanion.presentation.feature.spotscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Room
import com.example.campuscompanion.domain.usecase.GetRoomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val getRoomsUseCase: GetRoomsUseCase
) : ViewModel() {
    val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms = _rooms.asStateFlow()
    val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            _rooms.value = getRoomsUseCase()
            _isLoading.value = false
        }
    }
}