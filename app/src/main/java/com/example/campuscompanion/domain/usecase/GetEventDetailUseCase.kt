package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.repository.EventRepository
import javax.inject.Inject

class GetEventDetailUseCase @Inject constructor(
    private val eventRepository: EventRepository,
){
    suspend operator fun invoke(clubId: String, eventId: String) = eventRepository.getEventById(clubId, eventId)
}