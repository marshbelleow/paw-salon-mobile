package com.example.PawSalon.network

// Data model for Service Appointment request
data class ServiceAppointmentRequest(
    val first_name: String,
    val last_name: String,
    val phone: String,
    val email: String,
    val address: String,
    val furbabys_name: String,
    val pet_type: String,
    val service_type: String,
    val appointment_date: String,
    val appointment_time: String,
    val chosen_service: String
)