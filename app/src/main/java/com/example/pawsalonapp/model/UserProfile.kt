package com.example.pawsalonapp.model

data class UserProfile(
    val fullName: String,
    val username: String,
    val phoneNumber: String,
    val location: String,
    val gender: String,
    val avatar: String // Avatar URL or path
)