package com.example.PawSalon.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R
import com.example.PawSalon.network.ServiceAppointmentRequest
import com.example.PawSalon.network.ServiceAppointmentResponse
import com.example.PawSalon.view_models.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceConfirmationActivity : AppCompatActivity() {
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var email: String
    private lateinit var petType: String
    private lateinit var petName: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var serviceType: String
    private lateinit var specificService: String

    private lateinit var btnCancelAppointment: Button
    private lateinit var btnConfirmAppointment: Button
    private lateinit var tvConfirmationDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_confirmation)

        // Initialize UI components
        btnCancelAppointment = findViewById(R.id.cancel_button)
        btnConfirmAppointment = findViewById(R.id.confirm_button)
        tvConfirmationDetails = findViewById(R.id.confirmation_message) // Initialize TextView

        // Retrieve data from Intent
        intent.extras?.let {
            firstName = it.getString("FIRST_NAME", "")
            lastName = it.getString("LAST_NAME", "")
            phone = it.getString("PHONE", "")
            address = it.getString("ADDRESS", "")
            email = it.getString("EMAIL", "")
            petType = it.getString("PET_TYPE", "")
            petName = it.getString("PET_NAME", "")
            date = it.getString("DATE", "")
            time = it.getString("TIME", "")
            serviceType = it.getString("SERVICE_TYPE", "")
            specificService = it.getString("SPECIFIC_SERVICE", "")
        } ?: run {
            // Handle the case where no data was passed
            Toast.makeText(this, "No appointment details received", Toast.LENGTH_LONG).show()
            finish() // Optionally close the activity if no data is available
        }

        // Set confirmation details
        displayConfirmationDetails()

        btnConfirmAppointment.setOnClickListener {
            saveAppointmentToDatabase()
        }

        btnCancelAppointment.setOnClickListener {
            finish() // Close this activity or navigate back as needed
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

    private fun saveAppointmentToDatabase() {
        val appointmentRequest = ServiceAppointmentRequest(
            first_name = firstName,
            last_name = lastName,
            phone = phone,
            address = address,
            email = email,
            pet_type = petType,
            furbabys_name = petName,
            appointment_date = date,
            appointment_time = time,
            service_type = serviceType,
            chosen_service = specificService
        )

        val apiService = RetrofitInstance.api
        val sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")

        apiService.createServiceAppointment("Bearer $token", appointmentRequest)
            .enqueue(object : Callback<ServiceAppointmentResponse> {
                override fun onResponse(call: Call<ServiceAppointmentResponse>, response: Response<ServiceAppointmentResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ServiceConfirmationActivity, "Appointment confirmed!", Toast.LENGTH_SHORT).show()
                        // Navigate back to appointment activity or home
                        finish() // Close this activity
                    } else {
                        Toast.makeText(this@ServiceConfirmationActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.e("ServiceConfirmation", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ServiceAppointmentResponse>, t: Throwable) {
                    Toast.makeText(this@ServiceConfirmationActivity, "Failed to confirm appointment: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ServiceConfirmation", "Error: ${t.message}")
                }
            })
    }
}
