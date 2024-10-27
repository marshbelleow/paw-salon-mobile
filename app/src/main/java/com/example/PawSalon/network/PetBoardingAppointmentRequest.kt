// PawSalonApiService.kt
package com.example.PawSalon.network

data class BoardingAppointmentRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val petName: String,
    val petType: String,
    val optionType: String,
    val date: String,
    val time: String,
    val additionalDetails: String
)