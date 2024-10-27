package com.example.PawSalon.view_models

import com.example.PawSalon.view.ApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var retrofit: Retrofit? = null

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
                .build()
        }
        return retrofit!!
    }

    // Expose the API service
    val api: ApiService by lazy {
        getClient().create(ApiService::class.java)
    }
}
