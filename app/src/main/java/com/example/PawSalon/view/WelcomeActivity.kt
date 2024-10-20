package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)

        // Buttons for navigation
        val groomingButton: Button = findViewById(R.id.buttonGrooming)
        val petboardingButton: Button = findViewById(R.id.btnPetBoarding)
        val petsuppliesButton: Button = findViewById(R.id.btnPetSupplies)
        val basicvetservicesButton: Button = findViewById(R.id.btnVetService)

        // Navigate to Basic Vet Services Activity
        basicvetservicesButton.setOnClickListener {
            val intent = Intent(this, BasicVetServicesActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Grooming Activity
        groomingButton.setOnClickListener {
            val intent = Intent(this, GroomingActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Pet Boarding Activity
        petboardingButton.setOnClickListener {
            val intent = Intent(this, PetBoardingActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Pet Supplies Categorized Activity
                petsuppliesButton.setOnClickListener {
                    // Navigate to a categorized Pet Supplies section
                    val intent = Intent(this, PetSuppliesCatergorizedActivity::class.java)
                    startActivity(intent)
                }

        // Appointment button navigation
        val appointmentImageButton: ImageButton = findViewById(R.id.btn_appointment)
        appointmentImageButton.setOnClickListener {
            // Navigate directly to AppointmentActivity
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        }

        // Notifications button navigation
        val notificationsImageButton: ImageButton = findViewById(R.id.btn_notifications)
        notificationsImageButton.setOnClickListener {
            // Navigate directly to NotificationsActivity
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        // Profile button navigation
        val profileImageButton: ImageButton = findViewById(R.id.btn_profile)
        profileImageButton.setOnClickListener {
            // Navigate directly to ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Feedback button navigation
        val feedbackImageButton: ImageButton = findViewById(R.id.btn_feedback)
        feedbackImageButton.setOnClickListener {
            // Navigate directly to FeedbackActivity
            val intent = Intent(this, Feedback_Activity::class.java)
            startActivity(intent)
        }

        // Settings button navigation
        val settingsImageButton: ImageButton = findViewById(R.id.btn_settings)
        settingsImageButton.setOnClickListener {
            // Navigate directly to SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Home button navigation
        val homeButton: ImageButton = findViewById(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
