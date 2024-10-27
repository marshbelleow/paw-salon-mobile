package com.example.PawSalon.network

// Data model for signup request
data class SignUpRequest(
    val fullname: String,
    val username: String,
    val email: String,
    val password: String
)