package com.example.milsaboresapp.domain.model

data class TeamMember(
    val name: String,
    val role: String,
    val bio: String,
    val imageResName: String? = null
)
