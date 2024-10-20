package com.example.PawSalon.network

import com.example.PawSalon.model.Appointment
import com.example.PawSalon.model.UserProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.POST

// Data model for login request
data class LoginRequest(
    val username: String,
    val password: String
)

// Data model for login response
data class LoginResponse(
    val token: String?,
    val message: String?
)

// Data model for signup request
data class SignUpRequest(
    val fullname: String,
    val username: String,
    val email: String,
    val password: String
)

// Data model for signup response
data class SignUpResponse(
    val success: Boolean,
    val message: String
)

// Data model for forgot password request
data class ForgotPasswordRequest(
    val identifier: String
)

// Data model for forgot password response
data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val otpSent: Boolean
)

// Retrofit interface for API calls
interface ApiService {

    // Login API call
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Register API call
    @POST("register")
    fun signup(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    // Forgot password API call
    @POST("forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    // Get user profile by ID
    @GET("user/{id}")
    fun getUserProfile(@Path("id") userId: String): Call<UserProfile>

    // Update user profile by ID
    @PUT("user/{id}")
    fun updateUserProfile(@Path("id") userId: String, @Body updatedProfile: UserProfile): Call<UserProfile>



    // Create a new appointment
    @POST("appointments")
    fun createAppointment(@Body appointment: Appointment): Call<Appointment>

    // Get all appointments
    @GET("appointments")
    fun getAllAppointments(): Call<List<Appointment>>

    // Get a single appointment by ID
    @GET("appointments/{id}")
    fun getAppointmentById(@Path("id") id: Int): Call<Appointment>

    // Update an existing appointment
    @PUT("appointments/{id}")
    fun updateAppointment(@Path("id") id: Int, @Body appointment: Appointment): Call<Appointment>

    // Delete an appointment
    @DELETE("appointments/{id}")
    fun deleteAppointment(@Path("id") id: Int): Call<Void>
}