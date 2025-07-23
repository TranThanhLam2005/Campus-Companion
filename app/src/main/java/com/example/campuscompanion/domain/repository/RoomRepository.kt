package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Room

interface RoomRepository {
    suspend fun getAllRooms(): List<Room>
    suspend fun getRoomById(id: String): Room
}