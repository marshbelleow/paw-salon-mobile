package com.example.PawSalon.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R

class  ServiceConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_confirmation)

        // Find buttons by their IDs
        val cancelButton: Button = findViewById(R.id.cancel_button)
        val confirmButton: Button = findViewById(R.id.confirm_button)

        // Set click listener for Cancel Button
        cancelButton.setOnClickListener {
            cancelAppointment()
        }

        // Set click listener for Confirm Button
        confirmButton.setOnClickListener {
            showConfirmAppointmentDialog()
        }
    }

    // Method to handle the cancellation of an appointment
    private fun cancelAppointment() {
        Toast.makeText(this, "Appointment cancelled", Toast.LENGTH_SHORT).show()

        // Create an intent to navigate to the HomeScreenActivity
        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        // Finish current activity to prevent the user from going back here
        finish()
    }

    // Method to show a confirmation dialog for confirming the appointment
    private fun showConfirmAppointmentDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Appointment")
        builder.setMessage("Are you sure you want to confirm this appointment?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            // On confirmation, navigate to the home screen and show a success message
            Toast.makeText(this, "Appointment confirmed", Toast.LENGTH_SHORT).show()

            // Redirect to the home screen
            val intent = Intent(this, HomeScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

            // Finish current activity
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // Dismiss the dialog if the user cancels
            dialogInterface.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
