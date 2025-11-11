package com.example.milsaboresapp.domain.model

data class ContactInfo(
    val headline: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val businessHours: String,
    val serviceAreas: List<String>,
    val channels: List<ContactChannel>
)
