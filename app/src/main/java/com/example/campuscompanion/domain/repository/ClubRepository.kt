package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Club

interface ClubRepository {
    suspend fun getClubsByIds(ids: List<String>): List<Club>
    suspend fun getClubById(id: String): Club
}