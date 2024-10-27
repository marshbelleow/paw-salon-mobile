package com.example.PawSalon.network

import com.example.PawSalon.model.Feedback
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit interface for API calls
interface ApiService {

    // Login API call
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Register API call
    @POST("register")
    fun signup(@Body request: SignUpRequest): Call<SignUpResponse>

    // Service Appointment APIs
    // Create a new service appointment
    @POST("service-appointments")
    fun createServiceAppointment(@Body request: ServiceAppointmentRequest): Call<ServiceAppointmentResponse>

    // Boarding Appointment APIs
    // Create a new boarding appointment
    @POST("boarding-appointments")
    fun createBoardingAppointment(@Body request: PetBoardingAppointmentRequest): Call<PetBoardingAppointmentResponse>

    // Feedback
    @POST("submitFeedback") // Adjust the endpoint according to your API
    suspend fun submitFeedback(@Body feedback: Feedback): Response<Unit>
}
