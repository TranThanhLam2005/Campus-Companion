package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.NewFeed
import kotlinx.coroutines.flow.Flow

interface NewFeedRepository {
    fun observeAllFeeds(): Flow<List<NewFeed>>
    fun addCommentToFeed(feedId: String, comment: String): Flow<Unit>
}