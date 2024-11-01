package com.example.PawSalon.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R

class DogFoodActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dogfood)

        //BACK BUTTON
        val btnBack: ImageButton = findViewById(R.id.btn_back) // Ensure this ID matches your layout
        btnBack.setOnClickListener {
            val intent = Intent(this, PetSuppliesCatergorizedActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Appointment button navigation
        val appointmentImageButton: ImageButton = findViewById(R.id.btn_appointment)
        appointmentImageButton.setOnClickListener {
            val intent = Intent(this, ServiceAppointmentActivity::class.java)
            startActivity(intent)
        }


        // Feedback button navigation
        val feedbackImageButton: ImageButton = findViewById(R.id.btn_feedback)
        feedbackImageButton.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // Settings button navigation
        val settingsImageButton: ImageButton = findViewById(R.id.btn_settings)
        settingsImageButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Home button navigation
        val homeButton: ImageButton = findViewById(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
