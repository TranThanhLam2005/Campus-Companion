package com.example.campuscompanion.presentation.feature.spotscreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.usecase.GetCafeteriaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CafeteriaDetailViewModel @Inject constructor(
    private val getCafeteriaUseCase: GetCafeteriaUseCase
): ViewModel() {
    private val _cafeteria = MutableStateFlow<Cafeteria?>(null)
    val cafeteria = _cafeteria.asStateFlow()
    private val _isLoading =  MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadCafeteria(cafeteriaId: String){
        viewModelScope.launch {
            _isLoading.value = true
            _cafeteria.value = getCafeteriaUseCase(cafeteriaId)
            _isLoading.value = false
        }
    }
}