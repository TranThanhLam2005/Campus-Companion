package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.model.User

interface UserRepository {
    suspend fun getUser(): User
    suspend fun participantInEvent(eventId: String, clubId: String)
    suspend fun isParticipantInEvent(eventId: String): Boolean
    suspend fun getParticipatedEvents(): List<Event>
}