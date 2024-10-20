package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R

class AppointmentActivity : AppCompatActivity() {
    private lateinit var circleDog: ImageView
    private lateinit var circleCat: ImageView
    private lateinit var etDay: EditText
    private lateinit var etService: EditText // For the selected service category
    private lateinit var etServices: EditText // For the chosen service
    private lateinit var etServices5: EditText // For unlimited text input
    private lateinit var etTime: EditText // For preferred time
    private lateinit var arrowDropdown: ImageView
    private lateinit var arrowDateDropdown: ImageView // Arrow for date dropdown
    private lateinit var arrowTimeDropdown: ImageView // Arrow for time dropdown

    private var selectedMonth: String? = null
    private var selectedDay: String? = null
    private var selectedYear: String? = null
    private var selectedTime: String? = null

    // Define service categories and their respective options
    private val serviceOptions = mapOf(
        "Grooming Services" to arrayOf("Bath & Blow Dry", "Nail Clipping", "Ear Cleaning", "Face Trimming", "Sanitary Shaving", "Paw Trimming", "Teeth Brushing", "Basic Grooming", "Full Grooming"),
        "Pet Boarding" to arrayOf("Day Care (Hourly)", "Overnight or Longer Stay (Small-Medium)", "Overnight or Longer Stay (Large-Extra Large)"),
        "Basic Vet Services" to arrayOf("5 in 1 Vaccines", "Deworming", "Vitamins Administration", "Spay and Neuter", "Kennel cough")
    )

    private lateinit var selectedServiceCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

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

        // Home button navigation
        val homeButton: ImageButton = findViewById(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // Settings button navigation
        val settingsImageButton: ImageButton = findViewById(R.id.btn_settings)
        settingsImageButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Initialize the circles
        circleDog = findViewById(R.id.circleDog)
        circleCat = findViewById(R.id.circleCat)

        // Set click listeners for both circles
        circleDog.setOnClickListener {
            selectCircle(circleDog)
            deselectCircle(circleCat)
        }

        circleCat.setOnClickListener {
            selectCircle(circleCat)
            deselectCircle(circleDog)
        }

        // Initialize EditTexts and arrows for dropdowns
        etDay = findViewById(R.id.etDate)
        etService = findViewById(R.id.etservice) // For the selected service category
        etServices = findViewById(R.id.etservices) // For the chosen service
        etServices5 = findViewById(R.id.etservices5) // For unlimited text input
        etTime = findViewById(R.id.ettime) // For preferred time
        arrowDropdown = findViewById(R.id.arrowDropdown5)
        arrowDateDropdown = findViewById(R.id.arrowDropdown) // Arrow for date dropdown
        arrowTimeDropdown = findViewById(R.id.arrowDropdown10) // Arrow for time dropdown

        // Set click listener for the service dropdown arrow
        arrowDropdown.setOnClickListener {
            showServiceSelectionDialog()
        }

        // Set click listener for the date dropdown arrow
        arrowDateDropdown.setOnClickListener {
            showDateSelectionDialog()
        }

        // Set click listener for time selection
        arrowTimeDropdown.setOnClickListener {
            showTimeSelectionDialog()
        }

        // Initialize the Cancel button
        val cancelButton: ImageButton = findViewById(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            clearFields() // Call the function to clear fields
        }

        // Initialize the Confirm button
        val confirmButton: ImageButton = findViewById(R.id.btn_confirm)
        confirmButton.setOnClickListener {
            if (areFieldsFilled()) {
                // Proceed with the appointment confirmation logic (e.g., save data or navigate)
                Toast.makeText(this, "Appointment Confirmed!", Toast.LENGTH_SHORT).show()
                // Add your confirmation logic here
            } else {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }



    // Function to clear all input fields
    private fun clearFields() {
        etDay.text.clear()
        etService.text.clear()
        etServices.text.clear()
        etServices5.text.clear()
        etTime.text.clear()
        // Deselect the circles
        deselectCircle(circleDog)
        deselectCircle(circleCat)
    }

    // Function to check if all required fields are filled
    private fun areFieldsFilled(): Boolean {
        return etDay.text.isNotEmpty() &&
                etService.text.isNotEmpty() &&
                etServices.text.isNotEmpty() &&
                etServices5.text.isNotEmpty() &&
                etTime.text.isNotEmpty()
    }

    // Function to show the service category selection dialog
    private fun showServiceSelectionDialog() {
        val categories = serviceOptions.keys.toTypedArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Service Category")

        builder.setItems(categories) { _, which ->
            selectedServiceCategory = categories[which]
            etService.setText(selectedServiceCategory) // Set the selected category in etService
            showServiceOptionsDialog(selectedServiceCategory)
        }

        builder.show()
    }

    // Function to show the options for the selected service category
    private fun showServiceOptionsDialog(category: String) {
        val options = serviceOptions[category]

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select ${category}")

        builder.setItems(options) { _, which ->
            val selectedService = options!![which]
            etServices.setText(selectedService) // Display the chosen service in etServices
        }

        builder.show()
    }

    // Function to show the date selection dialog
    private fun showDateSelectionDialog() {
        val months = arrayOf(
            "Select Month", "January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Date")

        // Create a layout for the dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_date_picker, null)
        builder.setView(dialogView)

        // Initialize spinners for month, day, and year
        val monthSpinner = dialogView.findViewById<Spinner>(R.id.month_spinner)
        val daySpinner = dialogView.findViewById<Spinner>(R.id.day_spinner)
        val yearSpinner = dialogView.findViewById<Spinner>(R.id.year_spinner)

        // Setup adapters
        monthSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        val years = (2024..2034).map { it.toString() }.toTypedArray()
        yearSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)

        // Initialize day spinner with max possible days (1 to 31)
        val days = (1..31).map { it.toString() }.toTypedArray()
        daySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        daySpinner.setSelection(0) // Reset day selection

        // Set up the month selection listener
        monthSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) { // Ensure a month is selected
                    selectedMonth = months[position]
                    // If a year is already selected, populate the days
                    if (selectedYear != null) {
                        populateDaysForMonth(position, selectedYear!!.toInt(), daySpinner)
                    }
                } else {
                    selectedMonth = null
                    daySpinner.setSelection(0) // Reset day selection
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        })

        // Set up the year selection listener
        yearSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedYear = yearSpinner.selectedItem?.toString()
                if (selectedMonth != null) { // If a month is selected, populate the days
                    populateDaysForMonth(monthSpinner.selectedItemPosition, selectedYear!!.toInt(), daySpinner)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        })

        // Set up the dialog buttons
        builder.setPositiveButton("OK") { _, _ ->
            selectedDay = daySpinner.selectedItem?.toString()
            etDay.setText("$selectedMonth $selectedDay, $selectedYear") // Set date in EditText
        }
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    // Function to populate days based on selected month and year
    private fun populateDaysForMonth(monthPosition: Int, year: Int, daySpinner: Spinner) {
        val daysInMonth = when (monthPosition) {
            1, 3, 5, 7, 8, 10, 12 -> 31 // Months with 31 days
            4, 6, 9, 11 -> 30 // Months with 30 days
            2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28 // February
            else -> 31 // Default to 31 days
        }

        val days = (1..daysInMonth).map { it.toString() }.toTypedArray()
        daySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
    }

    // Function to show time selection dialog
    private fun showTimeSelectionDialog() {
        // Assuming you have predefined time slots, you can create a dialog like this
        val timeSlots = arrayOf("09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Preferred Time")
        builder.setItems(timeSlots) { _, which ->
            selectedTime = timeSlots[which]
            etTime.setText(selectedTime) // Set the selected time in etTime
        }

        builder.show()
    }

    // Function to select a circle (dog or cat)
    private fun selectCircle(circle: ImageView) {
        circle.setImageResource(R.drawable.circleselected) // Assuming you have a selected drawable
    }

    // Function to deselect a circle
    private fun deselectCircle(circle: ImageView) {
        circle.setImageResource(R.drawable.circleunselected) // Assuming you have an unselected drawable
    }
}
