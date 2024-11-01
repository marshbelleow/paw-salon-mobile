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
import com.example.PawSalon.network.ApiService
import com.example.PawSalon.view_models.RetrofitInstance

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
    private lateinit var timeDropdownBtn: ImageView
    private lateinit var dateDropdownBtn: ImageView
    private lateinit var serviceDropdownBtn: ImageView
    private lateinit var confirmBtn: ImageButton
    private lateinit var etServiceCategory: EditText
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

        // INITIALIZE UI
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
        timeDropdownBtn = findViewById(R.id.timeDropdown)
        dateDropdownBtn = findViewById(R.id.dateDropdown)
        serviceDropdownBtn = findViewById(R.id.serviceDropdown)
        confirmBtn = findViewById(R.id.btn_confirm)

        // API SERVICE
        apiService = RetrofitInstance.api

        // DATE, TIME, AND SERVICE
        dateDropdownBtn.setOnClickListener { showDateSelectionDialog() }
        timeDropdownBtn.setOnClickListener { showTimePicker() }
        serviceDropdownBtn.setOnClickListener { showServiceSelectionDialog() }
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

    // SETUP BOTTOM NAV BUTTON LISTENERS
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

        // FEEDBACK BUTTON
        findViewById<ImageButton>(R.id.btn_feedback)?.let { feedbackButton ->
            feedbackButton.setOnClickListener {
                startActivity(Intent(this, FeedbackActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        // BUTTON TINT
        findViewById<ImageButton>(R.id.btn_appointment)?.imageTintList =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_active)

        listOf(
            R.id.btn_home,
            R.id.btn_feedback
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
                selectedTime = String.format("%02d:%02d %s", hourFormatted, minute, amPm)
                etTime.setText(selectedTime)
            },
            currentHour,
            currentMinute,
            false
        )

        timePickerDialog.show()
    }

    private fun showServiceSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Service Category")
        val categories = serviceOptions.keys.toTypedArray()
        builder.setItems(categories) { _, which ->
            selectedServiceCategory = categories[which]
            etServiceCategory.setText(selectedServiceCategory)
            showSpecificServiceSelectionDialog(selectedServiceCategory)
        }
        builder.show()
    }

    private fun showSpecificServiceSelectionDialog(category: String) {
        val services = serviceOptions[category] ?: return
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Specific Service")
        builder.setItems(services) { _, which ->
            etChosenService.setText(services[which])
        }
        builder.show()
    }

    private fun submitAppointment() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()
        val address = etAddress.text.toString()
        val email = etEmail.text.toString()
        val petName = etPetName.text.toString()
        val animalType = if (animalSelectionGroup.checkedRadioButtonId == radioDog.id) "Dog" else "Cat"
        val date = etDate.text.toString()
        val time = etTime.text.toString()
        val serviceType = etServiceCategory.text.toString()
        val specificService = etChosenService.text.toString()

        val intent = Intent(this, ServiceConfirmationActivity::class.java).apply {
            putExtra("FIRST_NAME", firstName)
            putExtra("LAST_NAME", lastName)
            putExtra("PHONE", phoneNumber)
            putExtra("ADDRESS", address)
            putExtra("EMAIL", email)
            putExtra("PET_TYPE", animalType)
            putExtra("PET_NAME", petName)
            putExtra("DATE", date)
            putExtra("TIME", time)
            putExtra("SERVICE_TYPE", serviceType)
            putExtra("SPECIFIC_SERVICE", specificService)
        }
        startActivity(intent)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return Regex("^\\d{11}\$").matches(phoneNumber)
    }

    private fun isValidAddress(address: String): Boolean {
        return address.length >= 5
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
