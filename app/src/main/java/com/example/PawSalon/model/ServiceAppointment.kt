package com.example.PawSalon.model

data class ServiceAppointment(
    val client_name: String,
    val phone: String,
    val email: String,
    val address: String?,
    val furbabys_name: String,
    val pet_type: String,
    val serviceCategory: String,
    val chosenService: String,
    val date: String,
    val time: String,
    val additional_Details: String,
)
