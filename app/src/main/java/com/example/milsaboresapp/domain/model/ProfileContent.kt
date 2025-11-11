package com.example.milsaboresapp.domain.model

data class ProfileContent(
    val headline: String,
    val welcomeMessage: String,
    val personalSectionTitle: String,
    val preferencesSubtitle: String,
    val saveChangesLabel: String,
    val defaultShippingHelper: String,
    val defaultCouponHelper: String,
    val newsletterLabel: String,
    val saveAddressLabel: String,
    val passwordSectionTitle: String,
    val passwordSubmitLabel: String,
    val passwordHelper: String,
    val currentPasswordLabel: String,
    val newPasswordLabel: String,
    val confirmPasswordLabel: String,
    val personalData: ProfilePersonalData,
    val preferences: ProfilePreferences,
    val regions: List<ProfileRegion>,
    val shippingOptions: List<ProfileShippingOption>,
    val storedPassword: String
)
