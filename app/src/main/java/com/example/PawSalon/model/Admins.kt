package com.example.PawSalon.model

data class Admins(
    val id: Int,
    val username: String,
    val permissions: List<String>
)
