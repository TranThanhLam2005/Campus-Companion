package com.example.campuscompanion.domain.model

import java.util.UUID


data class NewFeed(
    val id: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val createdAt: Long = 0L,
    val comment: Map<String, String> = emptyMap()
)
