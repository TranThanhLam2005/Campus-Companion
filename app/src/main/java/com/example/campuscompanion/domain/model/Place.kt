package com.example.campuscompanion.domain.model

data class Place(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val lat:  Double = 0.0,
    val long:  Double = 0.0,
    val description: String = "",
    val star: Double = 0.0,
    val imageUrl: String = "",
)