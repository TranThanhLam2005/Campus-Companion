package com.example.campuscompanion.domain.model

import com.google.firebase.Timestamp

data class Event(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val date: Timestamp? = null,
    val location: String = "",
)
