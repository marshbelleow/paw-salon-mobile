package com.example.PawSalon.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R
import com.example.PawSalon.network.BoardingAppointmentRequest
import com.example.PawSalon.network.BoardingAppointmentResponse
import com.example.PawSalon.view_models.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetBoardingConfirmationActivity : AppCompatActivity() {

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var email: String
    private lateinit var petType: String
    private lateinit var petName: String
    private lateinit var checkInDate: String
    private lateinit var checkInTime: String
    private lateinit var petCheckIn: String
    private var days: Int = 0
    private var hours: Int = 0

    private lateinit var btnCancelAppointment: Button
    private lateinit var btnConfirmAppointment: Button
    private lateinit var tvConfirmationDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_boarding_confirmation)

        // Initialize UI components
        btnCancelAppointment = findViewById(R.id.cancel_button)
        btnConfirmAppointment = findViewById(R.id.confirm_button)
        tvConfirmationDetails = findViewById(R.id.confirmation_message)

        // Retrieve data from Intent
        intent.extras?.let {
            firstName = it.getString("FIRST_NAME", "")
            lastName = it.getString("LAST_NAME", "")
            phone = it.getString("PHONE", "")
            address = it.getString("ADDRESS", "")
            email = it.getString("EMAIL", "")
            petType = it.getString("PET_TYPE", "")
            petName = it.getString("PET_NAME", "")
            petCheckIn = it.getString("PET_CHECK_IN", "")
            checkInDate = it.getString("DATE", "")
            checkInTime = it.getString("TIME", "")
            days = it.getString("DAYS", "0").toIntOrNull() ?: 0 // Convert safely
            hours = it.getString("HOURS", "0").toIntOrNull() ?: 0 // Convert safely
        } ?: run {
            Toast.makeText(this, "No appointment details received", Toast.LENGTH_LONG).show()
            finish() // Optionally close the activity if no data is available
        }

        // Set confirmation details
        displayConfirmationDetails()

        btnConfirmAppointment.setOnClickListener {
            saveAppointment()
        }

        btnCancelAppointment.setOnClickListener {
            // Show confirmation dialog before canceling
            showCancelConfirmationDialog()
        }
    }

    private fun displayConfirmationDetails() {
        val confirmationMessage = """
            This message is to confirm your upcoming appointment.
        
            Please double-check that the information you provided is accurate, including your name, contact information, and the specific services you have booked.
        
            We want to remind you that there is a 10% additional fee for booking online and another 10% fee for last-minute cancellations. We understand that things come up, but we ask that you respect our time and commitment by providing accurate information and adhering to the cancellation policy.
        
            We take fraud seriously and will not hesitate to pursue legal action if necessary.
        
            Thank you for your understanding and cooperation.
        """.trimIndent()
        tvConfirmationDetails.text = confirmationMessage
    }

    private fun saveAppointment() {
        val appointmentRequest = BoardingAppointmentRequest(
            first_name = firstName,
            last_name = lastName,
            phone = phone,
            address = address,
            email = email,
            pet_type = petType,
            furbabys_name = petName,
            pet_check_in = petCheckIn,
            check_in_date = checkInDate,
            check_in_time = checkInTime,
            days = days,
            hours = hours
        )

        val apiService = RetrofitInstance.api
        val sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "") ?: ""

        apiService.createBoardingAppointment("Bearer $token", appointmentRequest)
            .enqueue(object : Callback<BoardingAppointmentResponse> {
                override fun onResponse(call: Call<BoardingAppointmentResponse>, response: Response<BoardingAppointmentResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PetBoardingConfirmationActivity, "Appointment confirmed!", Toast.LENGTH_SHORT).show()
                        finish() // Close this activity
                    } else {
                        Toast.makeText(this@PetBoardingConfirmationActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.e("PetBoardingConfirmation", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<BoardingAppointmentResponse>, t: Throwable) {
                    Toast.makeText(this@PetBoardingConfirmationActivity, "Failed to confirm appointment: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PetBoardingConfirmation", "Error: ${t.message}")
                }
            })
    }

    private fun showCancelConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to cancel the appointment?")
            .setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                finish() // Close this activity
            }
            .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss() // Just dismiss the dialog
            }
        builder.create().show()
    }
}
