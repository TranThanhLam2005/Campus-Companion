package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.User

interface UserRepository {
    suspend fun getUser(): User
}