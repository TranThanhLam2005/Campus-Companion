package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.repository.UserRepository
import javax.inject.Inject

class GetIsParticipantEventUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(eventId: String): Boolean {
        return repository.isParticipantInEvent(eventId)
    }
}