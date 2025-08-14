package com.example.campuscompanion.presentation.feature.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuscompanion.domain.model.NewFeed
import com.example.campuscompanion.domain.model.Room
import com.example.campuscompanion.domain.usecase.AddCommentFeedUseCase
import com.example.campuscompanion.domain.usecase.ObserveNewFeedUseCase
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeNewFeedUseCase: ObserveNewFeedUseCase,
    private val addCommentFeedUseCase: AddCommentFeedUseCase
): ViewModel() {
    val feeds: StateFlow<List<NewFeed>> = observeNewFeedUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun addCommentToFeed(feedId: String, comment: String) {
        viewModelScope.launch {
            addCommentFeedUseCase(feedId, comment)
        }
    }
}

