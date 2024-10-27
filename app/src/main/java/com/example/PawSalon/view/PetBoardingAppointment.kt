package com.example.PawSalon.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.PawSalon.R
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import java.util.*


class PetBoardingAppointment : AppCompatActivity() {
    // Declare views
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPetName: EditText
    private lateinit var radioGroupPets: RadioGroup
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

    private val calendar = Calendar.getInstance()
    private var selectedRadioButton: RadioButton? = null
    private var selectedOptionRadioButton: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_boarding_appointment)

        // Initialize views
        initializeViews()

        // Set listeners
        setListeners()
        setupButtonListeners()
    }

    private fun initializeViews() {
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etphone)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etPetName = findViewById(R.id.etfurbabyname)
        radioGroupPets = findViewById(R.id.radioGroupPets)
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
    }

    private fun setListeners() {
        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }
        etHmDays.setOnClickListener { showDayPicker() }
        etHours.setOnClickListener { showHoursPicker() }
        dateDropdown.setOnClickListener { showDatePicker() }
        timeDropdown.setOnClickListener { showTimePicker() }
        daysDropdown.setOnClickListener { showDayPicker() }
        hoursDropdown.setOnClickListener { showHoursPicker() }
        btnCancel.setOnClickListener { cancelAppointment() }
        btnConfirm.setOnClickListener { confirmAppointment() }

        // Set radio button listeners
        radioDog.setOnClickListener { handleRadioSelection(radioDog) }
        radioCat.setOnClickListener { handleRadioSelection(radioCat) }
        radioOption1.setOnClickListener { handleOptionSelection(radioOption1) }
        radioOption2.setOnClickListener { handleOptionSelection(radioOption2) }

        // Add text validation logic
        addTextChangeListeners()
    }

    private fun setupButtonListeners() {
        // Home button listener, if it exists
        findViewById<ImageButton>(R.id.btn_home)?.let { homeButton ->
            homeButton.setOnClickListener {
                val intent = Intent(this, HomeScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }

        // Feedback button listener, if it exists
        findViewById<ImageButton>(R.id.btn_feedback)?.let { feedbackButton ->
            feedbackButton.setOnClickListener {
                // Add your action for feedback here, e.g., starting FeedbackActivity
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

    private fun addTextChangeListeners() {
        etFirstName.addTextChangedListener(createTextWatcher("First name is required", etFirstName))
        etLastName.addTextChangedListener(createTextWatcher("Last name is required", etLastName))
        etPhone.addTextChangedListener(createTextWatcher("Phone number is required", etPhone))
        etEmail.addTextChangedListener(createTextWatcher("Email is required", etEmail))
        etAddress.addTextChangedListener(createTextWatcher("Address is required", etAddress))
        etPetName.addTextChangedListener(createTextWatcher("Pet name is required", etPetName))
    }

    private fun createTextWatcher(errorMessage: String, editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText.error = errorMessage
                } else {
                    editText.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
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

    private fun showDayPicker() {
        val days = arrayOf("1 Day", "2 Days", "3 Days", "4 Days", "5 Days")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Number of Days")
        builder.setItems(days) { _, which ->
            etHmDays.setText(days[which])
        }
        builder.show()
    }

    private fun showHoursPicker() {
        val hours = arrayOf("1 Hour", "2 Hours", "3 Hours", "4 Hours", "5 Hours")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Number of Hours")
        builder.setItems(hours) { _, which ->
            etHours.setText(hours[which])
        }
        builder.show()
    }

    private fun cancelAppointment() {
        clearFields()
        Toast.makeText(this, "Appointment cancelled", Toast.LENGTH_SHORT).show()
        // Redirect to HomeScreenActivity
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
        finish() // Optionally finish the current activity
    }

    private fun confirmAppointment() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val furbabyName = etPetName.text.toString().trim()
        val petType = when (selectedRadioButton) {
            radioDog -> "Dog"
            radioCat -> "Cat"
            else -> ""
        }
        val optionType = when (selectedOptionRadioButton) {
            radioOption1 -> "Option 1"
            radioOption2 -> "Option 2"
            else -> ""
        }
        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()
        val additionalDetails = etAdditionalDetails.text.toString().trim()

        // Validate inputs
        if (validateInputs(firstName, lastName, phone, email, address, furbabyName)) {
            // Confirm booking (API call or database logic can be added here)

            // Optionally pass data to the confirmation activity
            val intent = Intent(this, PetBoardingConfirmationActivity::class.java).apply {
                putExtra("FIRST_NAME", firstName)
                putExtra("LAST_NAME", lastName)
                putExtra("PHONE", phone)
                putExtra("EMAIL", email)
                putExtra("ADDRESS", address)
                putExtra("PET_NAME", furbabyName)
                putExtra("PET_TYPE", petType)
                putExtra("OPTION_TYPE", optionType)
                putExtra("DATE", date)
                putExtra("TIME", time)
                putExtra("ADDITIONAL_DETAILS", additionalDetails)
            }
            startActivity(intent)
            finish() // Optionally finish the current activity
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        address: String,
        furbabyName: String
    ): Boolean {
        return when {
            firstName.isEmpty() && lastName.isEmpty() && phone.isEmpty() &&
                    email.isEmpty() && address.isEmpty() && furbabyName.isEmpty() -> {
                showToast("All fields are required")
                false
            }
            firstName.isEmpty() -> {
                showToast("Please enter first name")
                false
            }
            lastName.isEmpty() -> {
                showToast("Please enter last name")
                false
            }
            !isValidPhone(phone) -> {
                showToast("Please enter a valid phone number")
                false
            }
            !isValidEmail(email) -> {
                showToast("Please enter a valid email address")
                false
            }
            address.isEmpty() -> {
                showToast("Please enter address")
                false
            }
            furbabyName.isEmpty() -> {
                showToast("Please enter pet name")
                false
            }
            else -> true
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return Pattern.matches("\\d{11}", phone)
    }

    private fun isValidEmail(email: String): Boolean {
        // Simple email validation pattern
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        )
        return emailPattern.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        etFirstName.text.clear()
        etLastName.text.clear()
        etPhone.text.clear()
        etEmail.text.clear()
        etAddress.text.clear()
        etPetName.text.clear()
        radioGroupPets.clearCheck()
        radioGroupOptions.clearCheck()
        etDate.text.clear()
        etTime.text.clear()
        etHmDays.text.clear()
        etHours.text.clear()
        etAdditionalDetails.text.clear()
    }

}
