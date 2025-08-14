package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Event

interface EventRepository {
    suspend fun getEventById(clubId: String, eventId: String): Event
}