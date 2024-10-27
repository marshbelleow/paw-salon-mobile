package com.example.PawSalon.view

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.PawSalon.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import com.example.PawSalon.network.ApiService
import com.example.PawSalon.network.ServiceAppointmentRequest
import com.example.PawSalon.network.ServiceAppointmentResponse
import com.example.PawSalon.view_models.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServiceAppointmentActivity : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var animalSelectionGroup: RadioGroup
    private lateinit var radioDog: RadioButton
    private lateinit var radioCat: RadioButton
    private lateinit var etPetName: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var cancelBtn: ImageButton
    private lateinit var confirmBtn: ImageButton
    private lateinit var etServiceCategory: EditText   // For service type/category
    private lateinit var etChosenService: EditText
    private val calendar = Calendar.getInstance()
    private var selectedTime: String? = null
    private lateinit var apiService: ApiService

    private val serviceOptions = mapOf(
        "Grooming Services" to arrayOf(
            "Bath & Blow Dry", "Nail Clipping", "Ear Cleaning",
            "Face Trimming", "Sanitary Shaving", "Paw Trimming",
            "Teeth Brushing", "Basic Grooming", "Full Grooming"
        ),
        "Basic Vet Services" to arrayOf(
            "5 in 1 Vaccines", "Deworming", "Vitamins Administration",
            "Spay and Neuter", "Kennel cough"
        )
    )

    private var selectedServiceCategory: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_appointment)

        // Initialize UI elements
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhoneNumber = findViewById(R.id.etphone)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        animalSelectionGroup = findViewById(R.id.radioGroupPets)
        radioDog = findViewById(R.id.radioDog)
        radioCat = findViewById(R.id.radioCat)
        etPetName = findViewById(R.id.etfurbabyname)
        etDate = findViewById(R.id.etDate)
        etServiceCategory = findViewById(R.id.etservice)
        etChosenService = findViewById(R.id.etSelectedService)
        etTime = findViewById(R.id.ettime)
        cancelBtn = findViewById(R.id.btn_cancel)
        confirmBtn = findViewById(R.id.btn_confirm)

        // Initialize API service
        apiService = RetrofitInstance.api

        // Date and time selection
        etDate.setOnClickListener { showDateSelectionDialog() }
        etTime.setOnClickListener { showTimePicker() }
        etServiceCategory.setOnClickListener { showServiceSelectionDialog() }

        setupButtonListeners()

        confirmBtn.setOnClickListener {
            val errors = validateFields()
            if (errors.isEmpty()) {
                submitAppointment()
            } else {
                displayErrors(errors)
            }
        }
    }

    // Setup bottom navigation button listeners
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

        // Settings button listener
        findViewById<ImageButton>(R.id.btn_feedback)?.let { feedbackButton ->
            feedbackButton.setOnClickListener {
                startActivity(Intent(this, FeedbackActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        // Adjust bottom navigation button tints
        findViewById<ImageButton>(R.id.btn_appointment)?.imageTintList =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_active)

        listOf(
            R.id.btn_home,
            R.id.btn_feedback,
            R.id.btn_notifications,
            R.id.btn_profile
        ).forEach { id ->
            findViewById<ImageButton>(id)?.imageTintList =
                ContextCompat.getColorStateList(this, R.color.bottom_nav_inactive)
        }
    }

    private fun validateFields(): List<String> {
        val errorMessages = mutableListOf<String>()

        // Check if all required fields are empty
        if (etFirstName.text.isEmpty() &&
            etLastName.text.isEmpty() &&
            etPhoneNumber.text.isEmpty() &&
            etAddress.text.isEmpty() &&
            etEmail.text.isEmpty() &&
            etPetName.text.isEmpty() &&
            etDate.text.isEmpty() &&
            etServiceCategory.text.isEmpty() &&
            etChosenService.text.isEmpty() &&
            etTime.text.isEmpty() &&
            animalSelectionGroup.checkedRadioButtonId == -1) {
            errorMessages.add("All fields are required.")
        } else {
            // Check individual fields if not all are empty
            if (etFirstName.text.isEmpty()) {
                errorMessages.add("First name is required.")
            }

            if (etLastName.text.isEmpty()) {
                errorMessages.add("Last name is required.")
            }

            if (etPhoneNumber.text.isEmpty()) {
                errorMessages.add("Phone number is required.")
            } else if (!isValidPhoneNumber(etPhoneNumber.text.toString())) {
                errorMessages.add("Please enter a valid phone number.")
            }

            if (etAddress.text.isEmpty()) {
                errorMessages.add("Address is required.")
            } else if (!isValidAddress(etAddress.text.toString())) {
                errorMessages.add("Please enter a valid address.")
            }

            if (etEmail.text.isEmpty()) {
                errorMessages.add("Email is required.")
            } else if (!isValidEmail(etEmail.text.toString())) {
                errorMessages.add("Please enter a valid email address.")
            }

            if (etPetName.text.isEmpty()) {
                errorMessages.add("Pet name is required.")
            }

            if (etDate.text.isEmpty()) {
                errorMessages.add("Date is required.")
            }

            if (etServiceCategory.text.isEmpty()) {
                errorMessages.add("Service category is required.")
            }

            if (etChosenService.text.isEmpty()) {
                errorMessages.add("Specific service is required.")
            }

            if (etTime.text.isEmpty()) {
                errorMessages.add("Time is required.")
            }

            if (animalSelectionGroup.checkedRadioButtonId == -1) {
                errorMessages.add("Please select an animal type.")
            }
        }

        // Clear all field errors when the form is valid
        if (errorMessages.isEmpty()) {
            etFirstName.error = null
            etLastName.error = null
            etPhoneNumber.error = null
            etAddress.error = null
            etEmail.error = null
            etPetName.error = null
            etDate.error = null
            etServiceCategory.error = null
            etChosenService.error = null
            etTime.error = null
        }

        return errorMessages
    }

    private fun displayErrors(errors: List<String>) {
        val errorMessage = errors.joinToString("\n")
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun showDateSelectionDialog() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            etDate.setText(dateFormat.format(selection))
        }
    }

    private fun showTimePicker() {
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val amPm = if (hourOfDay >= 12) "PM" else "AM"
                val hourFormatted = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourFormatted, minute, amPm)
                etTime.setText(selectedTime)
            }, currentHour, currentMinute, false
        )

        timePickerDialog.show()
    }

    private fun showServiceSelectionDialog() {
        val serviceCategories = serviceOptions.keys.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Select Service Category")
            .setItems(serviceCategories) { _, index ->
                selectedServiceCategory = serviceCategories[index]
                showSpecificServiceDialog(selectedServiceCategory)
            }
            .show()
    }

    private fun showSpecificServiceDialog(serviceCategory: String) {
        val specificServices = serviceOptions[serviceCategory]
        specificServices?.let { services ->
            AlertDialog.Builder(this)
                .setTitle("Select Specific Service")
                .setItems(services) { _, index ->
                    etChosenService.setText(services[index])
                    etServiceCategory.setText(serviceCategory)
                }
                .show()
        }
    }

    private fun submitAppointment() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val petName = etPetName.text.toString().trim()
        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()
        val animalType = if (radioDog.isChecked) "Dog" else "Cat"
        val serviceCategory = etServiceCategory.text.toString().trim() // New variable for service type
        val chosenService = etChosenService.text.toString().trim()    // For chosen specific service

        // Adjust for additional details if needed
        val appointmentRequest = ServiceAppointmentRequest(
            firstName, lastName, phoneNumber, address, email, petName, animalType, date, time, serviceCategory, chosenService)

        apiService.createServiceAppointment(appointmentRequest).enqueue(object : Callback<ServiceAppointmentResponse> {
            override fun onResponse(
                call: Call<ServiceAppointmentResponse>,
                response: Response<ServiceAppointmentResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ServiceAppointmentActivity, "Appointment submitted successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ServiceAppointmentActivity", "Error Response: $errorResponse")
                    Toast.makeText(this@ServiceAppointmentActivity, "Failed to submit appointment: $errorResponse", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ServiceAppointmentResponse>, t: Throwable) {
                Toast.makeText(this@ServiceAppointmentActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Validate phone number format
        return phoneNumber.matches(Regex("^\\+?[0-9]{10,13}\$"))
    }

    private fun isValidEmail(email: String): Boolean {
        // Validate email format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidAddress(address: String): Boolean {
        // Validate address (basic check for now)
        return address.isNotEmpty() && address.length > 5
    }
}
