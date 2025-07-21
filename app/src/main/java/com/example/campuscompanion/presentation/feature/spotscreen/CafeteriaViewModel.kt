package com.example.campuscompanion.presentation.feature.spotscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.usecase.GetCafeteriasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CafeteriaViewModel @Inject constructor(
    private val getCafeteriasUseCase: GetCafeteriasUseCase

) : ViewModel() {
    private val _cafeterias = MutableStateFlow<List<Cafeteria>>(emptyList())
    val cafeterias = _cafeterias.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadCafeterias(){
        viewModelScope.launch {
            if (_cafeterias.value.isNotEmpty()) return@launch
            _isLoading.value = true
            _cafeterias.value = getCafeteriasUseCase()
            _isLoading.value = false
        }
    }
}