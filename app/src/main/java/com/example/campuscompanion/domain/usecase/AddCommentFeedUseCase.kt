package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.repository.NewFeedRepository
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AddCommentFeedUseCase @Inject constructor(
    private val repository: NewFeedRepository
) {
    suspend operator fun invoke(
        feedId: String,
        comment: String
    ) {
        repository.addCommentToFeed(feedId, comment)
            .collect()
    }
}