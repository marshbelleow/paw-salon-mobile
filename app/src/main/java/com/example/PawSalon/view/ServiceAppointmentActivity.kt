package com.example.PawSalon.view

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
    private lateinit var etService: EditText
    private lateinit var etSelectedService: EditText
    private lateinit var cancelBtn: ImageButton
    private lateinit var confirmBtn: ImageButton
    private val calendar = Calendar.getInstance()
    private var selectedTime: String? = null

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
        etService = findViewById(R.id.etservice)
        etSelectedService = findViewById(R.id.etSelectedService)
        etTime = findViewById(R.id.ettime)
        cancelBtn = findViewById(R.id.btn_cancel)
        confirmBtn = findViewById(R.id.btn_confirm)

        // Date and time selection
        etDate.setOnClickListener { showDateSelectionDialog() }
        etTime.setOnClickListener { showTimePicker() }
        etService.setOnClickListener { showServiceSelectionDialog() }

        setupButtonListeners()
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
            etService.text.isEmpty() &&
            etSelectedService.text.isEmpty() &&
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

            if (etService.text.isEmpty()) {
                errorMessages.add("Service category is required.")
            }

            if (etSelectedService.text.isEmpty()) {
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
            etService.error = null
            etSelectedService.error = null
            etTime.error = null
        }

        return errorMessages
    }

    private fun displayErrors(errors: List<String>) {
        // Show the errors in Toasts
        for (error in errors) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        // Validates if the phone number is at least 10 digits long and contains only digits
        return phone.length >= 11 && phone.all { it.isDigit() }
    }

    private fun isValidEmail(email: String): Boolean {
        // Checks if the email matches the standard email format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidAddress(address: String): Boolean {
        // Checks if the address is not blank and has a minimum length
        return address.isNotBlank() && address.length >= 5
    }

    // Clear all form fields
    private fun clearFields() {
        etFirstName.text.clear()
        etLastName.text.clear()
        etPhoneNumber.text.clear()
        etAddress.text.clear()
        etEmail.text.clear()
        animalSelectionGroup.clearCheck()
        etPetName.text.clear()
        etDate.text.clear()
        etService.text.clear()
        etSelectedService.text.clear()
        etTime.text.clear()
    }

    // Function to display the date selection dialog
    private fun showDateSelectionDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Appointment Date")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            etDate.setText(sdf.format(Date(selection)))
        }
        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
    }

    // Function to display the time picker dialog
    private fun showTimePicker() {
        // Show an alert dialog with the availability note
        val availabilityDialog = AlertDialog.Builder(this)
            .setTitle("Salon Availability")
            .setMessage("Please note that the salon is available from 9 AM to 6 PM.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog and show the time picker
                val hour = calendar.get(Calendar.HOUR_OF_DAY) // Get current hour in 24-hour format
                val minute = calendar.get(Calendar.MINUTE)

                // Create the time picker dialog
                val timePicker = TimePickerDialog(
                    this,
                    { _, selectedHour, selectedMinute ->
                        // Format the hour and minute to 12-hour format with AM/PM
                        val amPm = if (selectedHour < 12) "AM" else "PM"
                        val hour12 = if (selectedHour == 0 || selectedHour == 12) 12 else selectedHour % 12 // Convert to 12-hour format
                        val format = String.format("%02d:%02d %s", hour12, selectedMinute, amPm)
                        etTime.setText(format)
                        selectedTime = format
                    },
                    hour,
                    minute,
                    false // Use false for 12-hour format
                )
                timePicker.show() // Show the time picker dialog
            }
            .setCancelable(false) // Make the dialog non-cancelable
            .create()

        availabilityDialog.show() // Show the availability note dialog
    }

    // Function to show a dialog to select the service
    private fun showServiceSelectionDialog() {
        val serviceDialogBuilder = AlertDialog.Builder(this)
        serviceDialogBuilder.setTitle("Select a Service Category")

        serviceDialogBuilder.setItems(serviceOptions.keys.toTypedArray()) { _, which ->
            selectedServiceCategory = serviceOptions.keys.elementAt(which)
            showSpecificServiceDialog(selectedServiceCategory)
        }

        serviceDialogBuilder.show()
    }

    // Function to show specific service options based on selected category
    private fun showSpecificServiceDialog(category: String) {
        val specificServiceDialogBuilder = AlertDialog.Builder(this)
        specificServiceDialogBuilder.setTitle("Select a Specific Service")

        specificServiceDialogBuilder.setItems(serviceOptions[category]) { _, which ->
            etService.setText(category)
            etSelectedService.setText(serviceOptions[category]?.get(which))
        }

        specificServiceDialogBuilder.show()
    }
}
