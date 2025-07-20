package com.example.campuscompanion.presentation.feature.clubscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.usecase.GetJoinedClubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubViewModel @Inject constructor(
    private val getJoinedClubsUseCase: GetJoinedClubsUseCase
): ViewModel() {
    private val _clubs = MutableStateFlow<List<Club>>(emptyList())
    val clubs = _clubs.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun loadJoinedClubs(){
        viewModelScope.launch {
            if (_clubs.value.isNotEmpty()) return@launch
            _isLoading.value = true
            _clubs.value = getJoinedClubsUseCase()
            _isLoading.value = false
        }

    }
}