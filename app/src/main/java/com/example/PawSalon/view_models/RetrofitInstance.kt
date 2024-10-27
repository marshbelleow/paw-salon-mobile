package com.example.PawSalon.view_models

import com.example.PawSalon.network.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private var retrofit: Retrofit? = null

    // Define the custom OkHttpClient with timeout configurations
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Adjust connection timeout
        .readTimeout(30, TimeUnit.SECONDS)     // Adjust read timeout
        .writeTimeout(30, TimeUnit.SECONDS)    // Adjust write timeout
        .build()

    // Function to get Retrofit client
    fun getClient(): Retrofit {
        if (retrofit == null) {
            // Create a Gson instance with setLenient()
            val gson = GsonBuilder()
                .setLenient() // Enable lenient parsing for potentially malformed JSON
                .create()

            // Build the Retrofit instance
            retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.43.116:80/api/") // Ensure this is correct
                .addConverterFactory(GsonConverterFactory.create(gson)) // Pass the lenient Gson instance here
                .client(okHttpClient)  // Attach the custom OkHttpClient
                .build()
        }
        return retrofit!!
    }

    // Expose the API service
    val api: ApiService by lazy {
        getClient().create(ApiService::class.java)
    }
}
