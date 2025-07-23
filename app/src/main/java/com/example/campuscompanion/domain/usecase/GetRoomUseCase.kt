package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Room
import com.example.campuscompanion.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomUseCase  @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(id: String): Room = roomRepository.getRoomById(id)
}