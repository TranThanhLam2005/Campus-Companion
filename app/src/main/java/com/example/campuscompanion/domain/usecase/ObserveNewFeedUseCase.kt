package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.NewFeed
import com.example.campuscompanion.domain.repository.NewFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNewFeedUseCase @Inject constructor(
    private val repository: NewFeedRepository
) {
    operator fun invoke(): Flow<List<NewFeed>> = repository.observeAllFeeds()
}