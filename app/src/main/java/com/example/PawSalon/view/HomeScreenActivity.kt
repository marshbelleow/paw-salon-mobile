package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.PawSalon.R

class HomeScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        // Buttons for navigation
        val groomingButton: Button = findViewById(R.id.buttonGrooming)
        val petboardingButton: Button = findViewById(R.id.btnPetBoarding)
        val petsuppliesButton: Button = findViewById(R.id.btnPetSupplies)
        val basicvetservicesButton: Button = findViewById(R.id.btnVetService)

        // Navigate to Basic Vet Services Activity
        basicvetservicesButton.setOnClickListener {
            navigateToActivity(BasicVetServicesActivity::class.java)
        }

        // Navigate to Grooming Activity
        groomingButton.setOnClickListener {
            navigateToActivity(GroomingActivity::class.java)
        }

        // Navigate to Pet Boarding Activity
        petboardingButton.setOnClickListener {
            navigateToActivity(PetBoardingActivity::class.java)
        }

        // Navigate to Pet Supplies Categorized Activity (Fixed Typo)
        petsuppliesButton.setOnClickListener {
            navigateToActivity(PetSuppliesCatergorizedActivity::class.java) // Corrected class name
        }

        // Setup bottom navigation icons
        setupBottomNav()

        // Button navigation
        setupNavigation(R.id.btn_appointment, ServiceAppointmentActivity::class.java)
        setupNavigation(R.id.btn_feedback, FeedbackActivity::class.java)
        setupNavigation(R.id.btn_settings, SettingsActivity::class.java)

        // Home button navigation with flags
        findViewById<ImageButton>(R.id.btn_home).setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    // Override to prevent navigating back to login/signup screen
    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    // Helper function to reduce redundancy for navigation
    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    // Helper function to set up image button navigation (removed notifications and profile)
    private fun setupNavigation(buttonId: Int, destinationActivity: Class<*>) {
        findViewById<ImageButton>(buttonId).setOnClickListener {
            navigateToActivity(destinationActivity)
        }
    }

    // Helper function to manage bottom navigation icons (removed notifications and profile)
    private fun setupBottomNav() {
        setBottomNavTint(R.id.btn_home, R.color.bottom_nav_active)
        setBottomNavTint(R.id.btn_appointment, R.color.bottom_nav_inactive)
        setBottomNavTint(R.id.btn_feedback, R.color.bottom_nav_inactive)
        setBottomNavTint(R.id.btn_settings, R.color.bottom_nav_active)
    }

    // Helper function to set tint for bottom navigation icons
    private fun setBottomNavTint(buttonId: Int, colorResId: Int) {
        findViewById<ImageButton>(buttonId).imageTintList =
            ContextCompat.getColorStateList(this, colorResId)
    }
}
