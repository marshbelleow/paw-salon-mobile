package com.example.PawSalon.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.PawSalon.R
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.PawSalon.network.ApiService
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import java.util.*
import com.example.PawSalon.view_models.RetrofitInstance


class PetBoardingAppointment : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPetName: EditText
    private lateinit var animalSelectionGroup: RadioGroup
    private lateinit var radioDog: RadioButton
    private lateinit var radioCat: RadioButton
    private lateinit var radioGroupOptions: RadioGroup
    private lateinit var radioOption1: RadioButton
    private lateinit var radioOption2: RadioButton
    private lateinit var etDate: EditText
    private lateinit var dateDropdown: ImageView
    private lateinit var etTime: EditText
    private lateinit var timeDropdown: ImageView
    private lateinit var etHmDays: EditText
    private lateinit var daysDropdown: ImageView
    private lateinit var etHours: EditText
    private lateinit var hoursDropdown: ImageView
    private lateinit var etAdditionalDetails: EditText
    private lateinit var btnCancel: ImageButton
    private lateinit var btnConfirm: ImageButton
    private lateinit var apiService: ApiService
    private val calendar = Calendar.getInstance()
    private var selectedRadioButton: RadioButton? = null
    private var selectedOptionRadioButton: RadioButton? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_boarding_appointment)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etphone)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etPetName = findViewById(R.id.etfurbabyname)
        animalSelectionGroup = findViewById(R.id.radioGroupPets)
        radioDog = findViewById(R.id.radioDog)
        radioCat = findViewById(R.id.radioCat)
        radioGroupOptions = findViewById(R.id.radioGroupOptions)
        radioOption1 = findViewById(R.id.radioOption1)
        radioOption2 = findViewById(R.id.radioOption2)
        etDate = findViewById(R.id.etDate)
        dateDropdown = findViewById(R.id.dateDropdown)
        etTime = findViewById(R.id.etTime)
        timeDropdown = findViewById(R.id.timeDropdown)
        etHmDays = findViewById(R.id.etHmDays)
        daysDropdown = findViewById(R.id.daysDropdown)
        etHours = findViewById(R.id.etHours)
        hoursDropdown = findViewById(R.id.hoursDropdown)
        etAdditionalDetails = findViewById(R.id.etspecifypb)
        btnCancel = findViewById(R.id.btn_cancel)
        btnConfirm = findViewById(R.id.btn_confirm)

        // API SERVICE
        apiService = RetrofitInstance.api

        setupButtonListeners()

        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }
        etHmDays.setOnClickListener { showDayPicker() }
        etHours.setOnClickListener { showHoursPicker() }
        dateDropdown.setOnClickListener { showDatePicker() }
        timeDropdown.setOnClickListener { showTimePicker() }
        daysDropdown.setOnClickListener { showDayPicker() }
        hoursDropdown.setOnClickListener { showHoursPicker() }
        btnCancel.setOnClickListener { cancelAppointment() }
        btnConfirm.setOnClickListener { submitAppointment() }

        // Set radio button listeners
        radioDog.setOnClickListener { handleRadioSelection(radioDog) }
        radioCat.setOnClickListener { handleRadioSelection(radioCat) }
        radioOption1.setOnClickListener { handleOptionSelection(radioOption1) }
        radioOption2.setOnClickListener { handleOptionSelection(radioOption2) }

        btnConfirm.setOnClickListener {
            val errors = validateFields()
            if (errors.isEmpty()) {
                submitAppointment()
            } else {
                displayErrors(errors)
            }
        }
    }


    private fun setupButtonListeners() {
        // HOME BUTTON
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
                // Add your action for feedback here, e.g., starting FeedbackActivity
                startActivity(Intent(this, FeedbackActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
        //BUTTON TINT
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
            etPhone.text.isEmpty() &&
            etAddress.text.isEmpty() &&
            etEmail.text.isEmpty() &&
            etPetName.text.isEmpty() &&
            etDate.text.isEmpty() &&
            etHours.text.isEmpty() &&
            etHmDays.text.isEmpty() &&
            etTime.text.isEmpty() &&
            animalSelectionGroup.checkedRadioButtonId == -1 &&
            radioGroupOptions.checkedRadioButtonId == -1
        ) {
            errorMessages.add("All fields are required.")
        } else {
            // Check individual fields if not all are empty
            if (etFirstName.text.isEmpty()) {
                errorMessages.add("First name is required.")
            }

            if (etLastName.text.isEmpty()) {
                errorMessages.add("Last name is required.")
            }

            if (etPhone.text.isEmpty()) {
                errorMessages.add("Phone number is required.")
            } else if (!isValidPhone(etPhone.text.toString())) {
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

            if (etHmDays.text.isEmpty()) {
                errorMessages.add("Service category is required.")
            }

            if (etHours.text.isEmpty()) {
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
            etPhone.error = null
            etAddress.error = null
            etEmail.error = null
            etPetName.error = null
            etDate.error = null
            etHmDays.error = null
            etHours.error = null
            etTime.error = null
        }

        return errorMessages
    }

    private fun displayErrors(errors: List<String>) {
        val errorMessage = errors.joinToString("\n")
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun showDatePicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Appointment Date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection
            val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            etDate.setText(format.format(calendar.time))
        }
    }

    private fun showTimePicker() {
        val availabilityDialog = AlertDialog.Builder(this)
            .setTitle("Salon Availability")
            .setMessage("Please note that booking for pet boarding is available from 9 AM to 6 PM.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    updateTimeField()
                }

                TimePickerDialog(
                    this,
                    timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
            }
            .setCancelable(false)
            .create()

        availabilityDialog.show()
    }

    private fun updateTimeField() {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        etTime.setText(sdf.format(calendar.time))
    }

    private fun handleRadioSelection(selectedRadio: RadioButton) {
        if (selectedRadioButton == selectedRadio) {
            selectedRadioButton?.isChecked = false
            selectedRadioButton = null
        } else {
            selectedRadioButton?.isChecked = false
            selectedRadioButton = selectedRadio
            selectedRadioButton?.isChecked = true
        }
    }

    private fun handleOptionSelection(selectedRadio: RadioButton) {
        if (selectedOptionRadioButton == selectedRadio) {
            selectedOptionRadioButton?.isChecked = false
            selectedOptionRadioButton = null
        } else {
            selectedOptionRadioButton?.isChecked = false
            selectedOptionRadioButton = selectedRadio
            selectedOptionRadioButton?.isChecked = true
        }
    }

    private fun showDayPicker() {
        val days = arrayOf("1 Day", "2 Days", "3 Days", "4 Days", "5 Days", "6 Days", "7 Days")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Number of Days")
        builder.setItems(days) { _, which ->
            etHmDays.setText(days[which].substringBefore(" "))
        }
        builder.show()
    }

    private fun showHoursPicker() {
        val hours = NumberPicker(this).apply {
            minValue = 1
            maxValue = 5 // Change this to the maximum number of hours you want to allow
            wrapSelectorWheel = true
        }

        // Set up the dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Number of Hours")
        builder.setView(hours) // Set the NumberPicker as the dialog view

        // Set the positive button to save the selection
        builder.setPositiveButton("OK") { _, _ ->
            val selectedHour = hours.value
            etHours.setText("$selectedHour Hour${if (selectedHour > 1) "s" else ""}")
        }

        // Set the negative button to cancel
        builder.setNegativeButton("Cancel", null)

        // Show the dialog
        builder.show()
    }

    private fun cancelAppointment() {
        Toast.makeText(this, "Appointment cancelled", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun submitAppointment() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val furbabyName = etPetName.text.toString().trim()
        val animalType = if (animalSelectionGroup.checkedRadioButtonId == radioDog.id) "Dog" else "Cat"
        val petCheckIn = if (radioGroupOptions.checkedRadioButtonId == radioOption1.id) "Option 1" else "Option 2"
        val checkInDate = etDate.text.toString().trim()
        val checkInTime = etTime.text.toString().trim()
        val stayDays = etHmDays.text.toString().toIntOrNull() ?: 1 // Default to 1 if empty or invalid
        val stayHours = etHours.text.toString().toIntOrNull() ?: 1

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() &&
            email.isNotEmpty() && address.isNotEmpty() && furbabyName.isNotEmpty() &&
            checkInDate.isNotEmpty() && checkInTime.isNotEmpty()) {

        // BOOKING REQUEST
        val intent = Intent(this, PetBoardingConfirmationActivity::class.java).apply {
            putExtra("FIRST_NAME", firstName)
            putExtra("LAST_NAME", lastName)
            putExtra("PHONE", phone)
            putExtra("ADDRESS", address)
            putExtra("EMAIL", email)
            putExtra("PET_TYPE", animalType)
            putExtra("PET_NAME", furbabyName)
            putExtra("PET_CHECK_IN", petCheckIn)
            putExtra("DATE", checkInDate)
            putExtra("TIME", checkInTime)
            putExtra("DAYS", stayDays)
            putExtra("HOURS", stayHours)
        }
        startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return Pattern.matches("\\d{11}", phone)
    }

    private fun isValidAddress(address: String): Boolean {
        return address.length >= 5
    }

    private fun isValidEmail(email: String): Boolean {
        // Simple email validation pattern
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        )
        return emailPattern.matcher(email).matches()
    }
}
