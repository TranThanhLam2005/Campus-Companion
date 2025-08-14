package com.example.campuscompanion.presentation.feature.clubscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.usecase.GetJoinedClubsUseCase
import com.example.campuscompanion.domain.usecase.GetRemainingClubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubViewModel @Inject constructor(
    private val getJoinedClubsUseCase: GetJoinedClubsUseCase,
    private val getRemainingClubsUseCase: GetRemainingClubsUseCase
): ViewModel() {
    private val _clubs = MutableStateFlow<List<Club>>(emptyList())
    private val _remainingClubs = MutableStateFlow<List<Club>>(emptyList())
    val remainingClubs = _remainingClubs.asStateFlow()
    val clubs = _clubs.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun loadJoinedClubs(){
        viewModelScope.launch {
            if (_clubs.value.isNotEmpty())
            _isLoading.value = true
            _clubs.value = getJoinedClubsUseCase()
            _isLoading.value = false
        }
    }
    fun loadRemainingClubs() {
        viewModelScope.launch {
            if (_remainingClubs.value.isNotEmpty())
            _isLoading.value = true
            try {
                _remainingClubs.value = getRemainingClubsUseCase()
            } catch (e: Exception) {
                Log.e("ClubViewModel", "Error loading remaining clubs: ${e.message}")
            }
            _isLoading.value = false
        }
    }
}