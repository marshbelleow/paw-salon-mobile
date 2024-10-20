package com.example.PawSalon.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.PawSalon.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var notificationsSwitch: SwitchCompat
    private lateinit var notificationStatusText: TextView
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("PawSalonPreferences", MODE_PRIVATE)

        // Initialize the notifications switch and status text
        notificationsSwitch = findViewById(R.id.switch_notifications)
        notificationStatusText = findViewById(R.id.text_notification_status)

        // Set the initial status text and switch state based on saved preference
        val isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", false)
        notificationsSwitch.isChecked = isNotificationsEnabled
        updateNotificationStatusText(isNotificationsEnabled)

        // Listener for the notifications switch
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateNotificationStatusText(isChecked)
            // Save the preference when the switch is toggled
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply()
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

        // Appointment button navigation
        val appointmentImageButton: ImageButton = findViewById(R.id.btn_appointment)
        appointmentImageButton.setOnClickListener {
            // Navigate directly to AppointmentActivity
            val intent = Intent(this, AppointmentActivity::class.java)
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

    // Function to update the notification status text
    private fun updateNotificationStatusText(isChecked: Boolean = notificationsSwitch.isChecked) {
        notificationStatusText.text = if (isChecked) "On" else "Off"
    }
}
