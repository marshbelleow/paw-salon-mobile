package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R

class GroomingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grooming)

        //BACK BUTTON
        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }

        //CUT AND STYLES BUTTON
        val seemoreButton: Button = findViewById(R.id.btn_SEEMORE)
        seemoreButton.setOnClickListener {
            val intent = Intent(this, CutStylesActivity::class.java)
            startActivity(intent)
        }

        // SETTINGS NAV
        val settingsImageButton: ImageButton = findViewById(R.id.btn_settings)
        settingsImageButton.setOnClickListener {
            // Navigate directly to SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // SERVICE APPOINTMENT NAV
        val appointmentImageButton: ImageButton = findViewById(R.id.btn_appointment)
        appointmentImageButton.setOnClickListener {
            // Navigate directly to AppointmentActivity
            val intent = Intent(this, ServiceAppointmentActivity::class.java)
            startActivity(intent)
        }

        // FEEDBACK NAV
        val feedbackImageButton: ImageButton = findViewById(R.id.btn_feedback)
        feedbackImageButton.setOnClickListener {
            // Navigate directly to FeedbackActivity
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // HOME NAV
        val homeButton: ImageButton = findViewById(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
