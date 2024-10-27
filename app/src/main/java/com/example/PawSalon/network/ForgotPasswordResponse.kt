package com.example.PawSalon.network

// Data model for forgot password response
data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val otpSent: Boolean
)