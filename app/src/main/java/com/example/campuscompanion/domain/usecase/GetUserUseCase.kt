package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.User
import com.example.campuscompanion.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): User = repository.getUser()
}
