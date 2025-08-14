package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Club

interface ClubRepository {
    suspend fun getClubsByIds(ids: List<String>): List<Club>
    suspend fun getClubById(id: String): Club

    suspend fun getRemainingClubs(userJoinedClubIds: List<String>): List<Club>

    suspend fun addMemberToClub(clubId: String, userId: String)
    suspend fun removeMemberFromClub(clubId: String, userId: String)
}