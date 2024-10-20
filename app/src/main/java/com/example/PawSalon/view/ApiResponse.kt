package com.example.PawSalon.view

import com.example.PawSalon.network.ForgotPasswordRequest
import com.example.PawSalon.network.ForgotPasswordResponse
import com.example.PawSalon.network.LoginRequest
import com.example.PawSalon.network.LoginResponse
import com.example.PawSalon.network.SignUpRequest
import com.example.PawSalon.network.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("clients/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("clients/register")
    fun signup(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("clients/forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>
}