package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.PawSalon.R

class FeedbackActivity : AppCompatActivity() {

    private lateinit var starButtons: List<ImageButton>
    private var currentRating = 0 // Variable to store the selected rating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        // Initialize the star buttons
        starButtons = listOf(
            findViewById(R.id.star1),
            findViewById(R.id.star2),
            findViewById(R.id.star3),
            findViewById(R.id.star4),
            findViewById(R.id.star5)
        )

        // Set click listeners for each star
        for (i in starButtons.indices) {
            starButtons[i].setOnClickListener {
                updateRating(i + 1) // Update rating when a star is clicked
            }
        }

        // Handle feedback submission
        val feedbackEditText = findViewById<EditText>(R.id.etspecifypb)
        val submitButton = findViewById<Button>(R.id.login_bigBtn)

        submitButton.setOnClickListener {
            handleSubmitFeedback(feedbackEditText)
        }

        // Set up navigation buttons
        setupButtonListeners()
    }

    // Function to update the star rating visuals
    private fun updateRating(rating: Int) {
        currentRating = rating
        for (i in starButtons.indices) {
            if (i < rating) {
                // Highlight the star (selected)
                starButtons[i].setImageResource(R.drawable.rating_selected)
            } else {
                // Unhighlight the star (unselected)
                starButtons[i].setImageResource(R.drawable.rating_unselected)
            }
        }
    }

    // Function to handle feedback submission
    private fun handleSubmitFeedback(feedbackEditText: EditText) {
        val feedbackText = feedbackEditText.text.toString().trim()

        if (feedbackText.isEmpty()) {
            Toast.makeText(this, "Please enter your feedback!", Toast.LENGTH_SHORT).show()
        } else if (currentRating == 0) {
            Toast.makeText(this, "Please select a rating!", Toast.LENGTH_SHORT).show()
        } else {
            // Here you would handle feedback without API call
            Toast.makeText(this, "Feedback Submitted: Rating $currentRating, Comment: $feedbackText", Toast.LENGTH_SHORT).show()
            feedbackEditText.text.clear()
            updateRating(0)
        }
    }

    // Function to set up button listeners for bottom navigation
    private fun setupButtonListeners() {
        // Home button listener
        findViewById<ImageButton>(R.id.btn_home)?.let { homeButton ->
            homeButton.setOnClickListener {
                val intent = Intent(this, HomeScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }

        // Appointment button listener
        findViewById<ImageButton>(R.id.btn_appointment)?.let { appointmentButton ->
            appointmentButton.setOnClickListener {
                val intent = Intent(this, ServiceAppointmentActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }

        // Settings button listener
        findViewById<ImageButton>(R.id.btn_settings)?.let { settingsButton ->
            settingsButton.setOnClickListener {
                startActivity(Intent(this, SettingsActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        // Set button tint for active and inactive states
        findViewById<ImageButton>(R.id.btn_feedback)?.imageTintList =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_active)

        listOf(
            R.id.btn_home,
            R.id.btn_appointment,
            R.id.btn_notifications,
            R.id.btn_profile
        ).forEach { id ->
            findViewById<ImageButton>(id)?.imageTintList =
                ContextCompat.getColorStateList(this, R.color.bottom_nav_inactive)
        }
    }
}
