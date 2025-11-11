package com.example.milsaboresapp.domain.model

data class AboutInfo(
    val headline: String,
    val subtitle: String,
    val mission: String,
    val vision: String,
    val timeline: List<TimelineEvent>,
    val values: List<ValueItem>,
    val team: List<TeamMember>,
    val achievements: List<Achievement>,
    val callToActionMessage: String,
    val callToActionButton: String
)
