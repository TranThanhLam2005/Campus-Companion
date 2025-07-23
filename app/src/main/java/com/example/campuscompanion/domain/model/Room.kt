package com.example.campuscompanion.domain.model

import com.google.firebase.Timestamp

data class Room(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val availableTime: Timestamp? = null,
)
