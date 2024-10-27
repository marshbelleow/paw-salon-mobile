package com.example.PawSalon.model

data class ServiceAppointment(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String?,
    val furbabysName: String,
    val petType: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val serviceType: String,     // Not repeated
    val chosenService: String,
    val additionalDetails: String? // Follow camelCase for consistency
)