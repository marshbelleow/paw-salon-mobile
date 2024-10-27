package com.example.PawSalon.network

// Data model for Service Appointment request
data class ServiceAppointmentRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val petName: String,
    val petType: String,
    val serviceType: String,
    val date: String,
    val time: String,
    val additionalDetails: String
)