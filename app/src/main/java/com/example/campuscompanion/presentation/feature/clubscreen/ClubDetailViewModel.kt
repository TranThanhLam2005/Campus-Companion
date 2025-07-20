package com.example.campuscompanion.presentation.feature.clubscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.usecase.GetClubUseCase
import com.example.campuscompanion.domain.usecase.GetJoinedClubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    private val getClubUseCase: GetClubUseCase
): ViewModel() {
    private val _club = MutableStateFlow<Club?>(null)
    val club = _club.asStateFlow()
    private val _isLoading =  MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadClub(clubId: String){
        viewModelScope.launch {
            _isLoading.value = true
            _club.value = getClubUseCase(clubId)
            _isLoading.value = false
        }

    }
}