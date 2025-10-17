package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.repository.UserRepository
import javax.inject.Inject

class GetParticipantEventsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<Event> {
        return userRepository.getParticipatedEvents()
    }
}