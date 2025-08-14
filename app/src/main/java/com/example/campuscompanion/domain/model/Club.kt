package com.example.campuscompanion.domain.model

data class Club(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val location: String = "",
    val description: String = "",
    val socialLink: String = "",
    val imageUrl: String = "",
    val memberList: List<String> = emptyList(),
    val events: List<Event> = emptyList(),
)