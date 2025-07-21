package com.example.campuscompanion.domain.usecase

import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.repository.CafeteriaRepository
import javax.inject.Inject

class GetCafeteriasUseCase @Inject constructor(
    private val repository: CafeteriaRepository
){
    suspend operator fun invoke(): List<Cafeteria> {
        return repository.getAllCafeterias()
    }

}