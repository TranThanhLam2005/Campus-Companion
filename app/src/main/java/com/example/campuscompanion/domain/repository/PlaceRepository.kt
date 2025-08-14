package com.example.campuscompanion.domain.repository

import com.example.campuscompanion.domain.model.Place

interface PlaceRepository {
    suspend fun getPlaces(): List<Place>
}