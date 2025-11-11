package com.example.milsaboresapp.domain.model.admin

import com.example.milsaboresapp.domain.model.ProfileRegion

data class AdminUserFormContent(
    val title: String,
    val subtitle: String,
    val roles: List<String>,
    val regions: List<ProfileRegion>,
    val successMessage: String
)
