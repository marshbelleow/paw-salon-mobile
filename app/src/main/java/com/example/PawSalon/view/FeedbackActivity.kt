package com.example.PawSalon.view

import android.content.Intent
import android.content.SharedPreferences // Add this import
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope // Import lifecycleScope
import com.example.PawSalon.R
import com.example.PawSalon.network.Feedback
import com.example.PawSalon.view_models.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FeedbackActivity : AppCompatActivity() {
    companion object {
        private const val MAX_RATING = 5
    }

    private lateinit var starButtons: List<ImageButton>
    private var currentRating = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        //INITIALIZE PAW BUTTONS
        starButtons = listOf(
            findViewById(R.id.star1),
            findViewById(R.id.star2),
            findViewById(R.id.star3),
            findViewById(R.id.star4),
            findViewById(R.id.star5)
        )

        starButtons.forEachIndexed { index, button ->
            button.setOnClickListener { updateRating(index + 1) }
        }

        val feedbackEditText = findViewById<EditText>(R.id.etspecifypb)
        val submitButton = findViewById<Button>(R.id.login_bigBtn)

        submitButton.setOnClickListener {
            handleSubmitFeedback(feedbackEditText)
        }

        setupButtonListeners()
    }

    private fun updateRating(rating: Int) {
        currentRating = rating
        starButtons.forEachIndexed { index, button ->
            button.setImageResource(
                if (index < rating) R.drawable.rating_selected
                else R.drawable.rating_unselected
            )
        }
    }

    private fun handleSubmitFeedback(feedbackEditText: EditText) {
        val feedbackText = feedbackEditText.text.toString().trim()

        when {
            feedbackText.isEmpty() -> showToast("Please enter your feedback!")
            currentRating == 0 -> showToast("Please select a rating!")
            else -> {
                val feedback = Feedback(currentRating, feedbackText)

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        // Retrieve the Bearer token from SharedPreferences
                        val sharedPreferences: SharedPreferences = getSharedPreferences("YourPrefsName", MODE_PRIVATE)
                        val token = sharedPreferences.getString("TOKEN_KEY", null)

                        // Pass the token in the header
                        if (token != null) { // Check if the token is not null
                            val response = RetrofitInstance.api.submitFeedback(token, feedback)

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    showToast("Feedback Submitted: Rating $currentRating, Comment: $feedbackText")
                                    feedbackEditText.text.clear()
                                    updateRating(0)
                                } else {
                                    showToast("Submission failed: ${response.message()}")
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                showToast("Token not found. Please log in again.")
                            }
                        }
                    } catch (e: HttpException) {
                        withContext(Dispatchers.Main) {
                            showToast("Submission error: ${e.message()}")
                        }
                    } catch (e: Exception) { // Catch any other exceptions
                        withContext(Dispatchers.Main) {
                            showToast("Unexpected error: ${e.localizedMessage}")
                        }
                    }
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupButtonListeners() {
        //home nav
        findViewById<ImageButton>(R.id.btn_home)?.setOnClickListener {
            navigateTo(HomeScreenActivity::class.java)
        }
        //appointment nav
        findViewById<ImageButton>(R.id.btn_appointment)?.setOnClickListener {
            navigateTo(ServiceAppointmentActivity::class.java)
        }
        //settings nav
        findViewById<ImageButton>(R.id.btn_settings)?.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
        //tint buttons
        findViewById<ImageButton>(R.id.btn_feedback)?.imageTintList =
            ContextCompat.getColorStateList(this, R.color.bottom_nav_active)

        listOf(
            R.id.btn_home,
            R.id.btn_appointment
        ).forEach { id ->
            findViewById<ImageButton>(id)?.imageTintList =
                ContextCompat.getColorStateList(this, R.color.bottom_nav_inactive)
        }
    }

    private fun <T> navigateTo(activity: Class<T>) {
        val intent = Intent(this, activity).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
