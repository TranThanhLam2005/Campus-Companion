package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Place
import com.example.campuscompanion.domain.repository.PlaceRepository
import javax.inject.Inject

class GetNearbyPlaceUseCase @Inject constructor(
    private val placeRepository: PlaceRepository
) {

    suspend operator fun invoke(): List<Place> = placeRepository.getPlaces()

}