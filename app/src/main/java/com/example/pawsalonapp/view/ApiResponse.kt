package com.example.pawsalonapp.view

import com.example.pawsalonapp.network.ForgotPasswordRequest
import com.example.pawsalonapp.network.ForgotPasswordResponse
import com.example.pawsalonapp.network.LoginRequest
import com.example.pawsalonapp.network.LoginResponse
import com.example.pawsalonapp.network.SignUpRequest
import com.example.pawsalonapp.network.SignUpResponse
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