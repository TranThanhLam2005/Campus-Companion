package com.example.campuscompanion.domain.model

data class User(
    val id: String = "",
    val fullName: String,
    val email: String,
    val contact: String,
    val joinedClubs: List<String> = emptyList()
)
