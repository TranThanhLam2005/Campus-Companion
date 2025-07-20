package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.repository.ClubRepository
import com.example.campuscompanion.domain.repository.UserRepository
import javax.inject.Inject

class GetJoinedClubsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val clubRepository: ClubRepository,
){
    suspend operator fun invoke(): List<Club>{
        val user = userRepository.getUser()
        return clubRepository.getClubsByIds(user.joinedClubs)
    }
}