package com.example.campuscompanion.presentation.feature.profilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.User
import com.example.campuscompanion.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel(){
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow();

    private val _isLoading =  MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun loadUser(){
        viewModelScope.launch {
            _isLoading.value = true
            _user.value = getUserUseCase()
            _isLoading.value = false
        }
    }
}