package com.example.PawSalon.network

import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

// Retrofit interface for API calls
interface ApiService {

    @Headers("Content-Type: application/json")
    // Login API call
    @POST("clients/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Register API call
    @POST("clients/signup")
    fun signup(@Body request: SignUpRequest): Call<SignUpResponse>

    // Service Appointment APIs
    // Create a new service appointment with Authorization header for authentication
    @POST("appointments")
    fun createServiceAppointment(
        @Header("Authorization") token: String,
        @Body request: ServiceAppointmentRequest
    ): Call<ServiceAppointmentResponse>

    // Pet Boarding Appointment APIs
    @POST("pet-boardings")
    fun createBoardingAppointment(
        @Header("Authorization") token: String,
        @Body request: BoardingAppointmentRequest
    ): Call<BoardingAppointmentResponse>

    // Feedback submission API, using suspend for coroutine support
    @POST("reviews")
    suspend fun submitFeedback(
        @Header("Authorization") token: String,
        @Body feedback: Feedback
    ): Response<Unit>
}
