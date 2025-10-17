package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.repository.UserRepository
import javax.inject.Inject

class ParticipantEventUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(eventId: String, clubId: String) {
        return repository.participantInEvent(eventId, clubId)
    }
}