    package com.example.PawSalon.network

    import com.example.PawSalon.model.Feedback
    import retrofit2.Response
    import retrofit2.Call
    import retrofit2.http.Body
    import retrofit2.http.DELETE
    import retrofit2.http.GET
    import retrofit2.http.PUT
    import retrofit2.http.Path
    import retrofit2.http.POST

    // Retrofit interface for API calls
    interface ApiService {

        // Login API call
        @POST("login")
        fun login(@Body request: LoginRequest): Call<LoginResponse>

        // Register API call
        @POST("register")
        fun signup(@Body request: SignUpRequest): Call<SignUpResponse>

        // Forgot password API call
        @POST("forgot-password")
        fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

        // Service Appointment APIs
        // Create a new service appointment
        @POST("service-appointments")
        fun createServiceAppointment(@Body request: ServiceAppointmentRequest): Call<ServiceAppointmentResponse>

        // Get all service appointments
        @GET("service-appointments")
        fun getAllServiceAppointments(): Call<List<ServiceAppointmentResponse>>

        // Get a single service appointment by ID
        @GET("service-appointments/{id}")
        fun getServiceAppointmentById(@Path("id") id: Int): Call<ServiceAppointmentResponse>

        // Update an existing service appointment
        @PUT("service-appointments/{id}")
        fun updateServiceAppointment(@Path("id") id: Int, @Body request: ServiceAppointmentRequest): Call<ServiceAppointmentResponse>

        // Delete a service appointment
        @DELETE("service-appointments/{id}")
        fun deleteServiceAppointment(@Path("id") id: Int): Call<Void>



        // Boarding Appointment APIs
        // Create a new boarding appointment
        @POST("boarding-appointments")
        fun createBoardingAppointment(@Body request: BoardingAppointmentRequest): Call<BoardingAppointmentResponse>

        // Get all boarding appointments
        @GET("boarding-appointments")
        fun getAllBoardingAppointments(): Call<List<BoardingAppointmentResponse>>

        // Get a single boarding appointment by ID
        @GET("boarding-appointments/{id}")
        fun getBoardingAppointmentById(@Path("id") id: Int): Call<BoardingAppointmentResponse>

        // Update an existing boarding appointment
        @PUT("boarding-appointments/{id}")
        fun updateBoardingAppointment(@Path("id") id: Int, @Body request: BoardingAppointmentRequest): Call<BoardingAppointmentResponse>

        // Delete a boarding appointment
        @DELETE("boarding-appointments/{id}")
        fun deleteBoardingAppointment(@Path("id") id: Int): Call<Void>

        //FEEDBACK
        @POST("feedback")
        suspend fun submitFeedback(@Body feedback: Feedback): Response<Void>
    }
