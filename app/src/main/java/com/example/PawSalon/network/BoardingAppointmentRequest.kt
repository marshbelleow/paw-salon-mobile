package com.example.PawSalon.network

// Data model for Boarding Appointment request
data class BoardingAppointmentRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val petName: String,
    val petType: String,
    val boardingType: String,
    val date: String,
    val time: String,
    val additionalDetails: String
)