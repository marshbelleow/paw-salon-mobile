package com.example.PawSalon.network

data class BoardingAppointmentRequest(
    val first_name: String,
    val last_name: String,
    val phone: String,
    val email: String,
    val address: String,
    val furbabys_name: String,
    val pet_type: String,
    val pet_check_in: String,
    val check_in_date: String,
    val check_in_time: String,
    val days: Int,
    val hours: Int,
)
