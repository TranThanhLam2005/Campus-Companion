package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.model.Order

interface CafeteriaRepository {
    suspend fun getAllCafeterias(): List<Cafeteria>
    suspend fun getCafeteriaById(id: String): Cafeteria
}