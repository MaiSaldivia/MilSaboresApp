package com.example.milsaboresapp.domain.model

data class BlogDetailSection(
    val heading: String,
    val paragraphs: List<String>,
    val bulletPoints: List<String> = emptyList()
)
