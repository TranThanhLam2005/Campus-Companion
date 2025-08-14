package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.repository.ClubRepository
import javax.inject.Inject


class UserUnFollowUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: String, userId: String) = clubRepository.removeMemberFromClub(clubId, userId)
}