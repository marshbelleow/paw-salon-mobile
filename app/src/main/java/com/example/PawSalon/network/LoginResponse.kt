package com.example.PawSalon.network

// Data model for login response
data class LoginResponse(
    val token: String?,
    val message: String?
) {
}