package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.repository.ClubRepository
import javax.inject.Inject

class UserFollowClubUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: String, userId: String) = clubRepository.addMemberToClub(clubId, userId)

}