package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Cafeteria

interface CafeteriaRepository {
    suspend fun getAllCafeterias(): List<Cafeteria>
    suspend fun getCafeteriaById(id: String): Cafeteria
}