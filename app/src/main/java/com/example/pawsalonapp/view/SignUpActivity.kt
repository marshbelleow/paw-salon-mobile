package com.example.pawsalonapp.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalonapp.R
import com.example.pawsalonapp.view_models.RetrofitInstance
import com.example.pawsalonapp.databinding.ActivitySignupBinding
import com.example.pawsalonapp.network.SignUpRequest
import com.example.pawsalonapp.network.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Activity for handling the sign-up process
class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    // Binding object to access views in the activity
    private lateinit var mBinding: ActivitySignupBinding

    // Input filter to prevent spaces in the input fields
    private val noSpacesInputFilter = InputFilter { source, start, end, dest, dstart, dend ->
        if (source.toString().contains(" ")) {
            // Return empty string if a space is detected
            ""
        } else {
            null
        }
    }

    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this activity
        mBinding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        // Apply the no spaces input filter to the password fields to prevent spaces
        mBinding.signupPasswordEt.filters = arrayOf(noSpacesInputFilter)
        mBinding.signupCPasswordEt.filters = arrayOf(noSpacesInputFilter)

        // Set focus change listeners for form fields to trigger validation
        mBinding.signupUsernameEt.onFocusChangeListener = this
        mBinding.signupPasswordEt.onFocusChangeListener = this
        mBinding.signupCPasswordEt.onFocusChangeListener = this
        mBinding.fullnameSetupEt.onFocusChangeListener = this
        mBinding.signupEmailEt.onFocusChangeListener = this

        // Handle navigation to the login screen when the sign-in button is clicked
        mBinding.signInButton.setOnClickListener {
            navigateToLogin()
        }

        // Handle the sign-up button click event
        mBinding.signupBigBtn.setOnClickListener {
            // Check if all fields are valid before making the API call
            if (validateAllFields()) {
                // Collect input data from the form fields
                val fullName = mBinding.fullnameSetupEt.text.toString().trim()
                val username = mBinding.signupUsernameEt.text.toString().trim()
                val email = mBinding.signupEmailEt.text.toString().trim()
                val password = mBinding.signupPasswordEt.text.toString().trim()

                // Create the request object for the sign-up API call
                val registerRequest = SignUpRequest(fullName, username, email, password)

                // Get the Retrofit API service instance
                val apiService = RetrofitInstance.getClient().create(ApiService::class.java)

                // Make the sign-up API call
                apiService.signup(registerRequest).enqueue(object: Callback<SignUpResponse> {
                    // Handle the API response
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful) {
                            // If the sign-up is successful, show a toast and navigate to the login screen
                            Toast.makeText(this@SignUpActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            // If there's an error, display the error message in a toast
                            Toast.makeText(this@SignUpActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Handle network failure or error
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        // Display a network error message
                        Toast.makeText(this@SignUpActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                // If form validation fails, show an error message
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Navigates to the login screen, clearing the current task stack
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    // Validates all fields in the form and returns true if all are valid
    private fun validateAllFields(): Boolean {
        val isFullNameValid = validateFullName()      // Validate full name
        val isEmailValid = validateEmail()            // Validate email
        val isUsernameValid = validateUsername()      // Validate username
        val isPasswordValid = validatePassword() && validatePasswordAndConfirmPassword() && validateConfirmPassword()  // Validate password and confirmation

        // Return true if all validations passed
        return isFullNameValid && isEmailValid && isUsernameValid && isPasswordValid
    }

    // Validates the full name field
    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val firstName: String = mBinding.fullnameSetupEt.text.toString().trim()

        // Check if the full name is empty
        if (firstName.isEmpty()) {
            errorMessage = "First Name is required"
        }

        // Set error message if validation failed
        if (errorMessage != null) {
            mBinding.fullnameSetupTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.fullnameSetupTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validates the email field
    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val email: String = mBinding.signupEmailEt.text.toString().trim()

        // Check if the email is empty or invalid
        if (email.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Enter a valid email address"
        }

        // Set error message if validation failed
        if (errorMessage != null) {
            mBinding.signupEmailTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupEmailTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validates the username field
    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val username: String = mBinding.signupUsernameEt.text.toString().trim()

        // Check if the username is valid based on various criteria
        if (username.isEmpty()) {
            errorMessage = "Username is required"
        } else if (username.length < 6) {
            errorMessage = "Username must be at least 6 characters long"
        } else if (!username.matches(Regex("^[a-zA-Z0-9_.]+$"))) {
            errorMessage = "Username can only contain letters, numbers, underscores, and periods"
        } else if (username.startsWith("_") || username.startsWith(".") || username.endsWith("_") || username.endsWith(".")) {
            errorMessage = "Username cannot start or end with special characters"
        } else if (!isUniqueUsername(username)) {
            errorMessage = "Username is already taken"
        }

        // Set error message if validation failed
        if (errorMessage != null) {
            mBinding.signupUsernameTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupUsernameTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validates the password field
    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val password = mBinding.signupPasswordEt.text.toString().trim()

        // Check if the password is empty or too short
        if (password.isEmpty()) {
            errorMessage = "Password is required"
        } else if (password.length < 8) {
            errorMessage = "Password must be at least 8 characters long"
        }

        // Set error message if validation failed
        if (errorMessage != null) {
            mBinding.signupPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupPasswordTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validates the confirm password field
    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val confirmPassword = mBinding.signupCPasswordEt.text.toString().trim()

        // Check if confirm password is empty
        if (confirmPassword.isEmpty()) {
            errorMessage = "Confirm Password is required"
        }

        // Set error message if validation failed
        if (errorMessage != null) {
            mBinding.signupCPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupCPasswordTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validates that password and confirm password fields match
    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = mBinding.signupPasswordEt.text.toString().trim()
        val confirmPassword = mBinding.signupCPasswordEt.text.toString().trim()

        // Check if passwords match
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
        }

        // Set error message or show a success icon if passwords match
        if (errorMessage != null) {
            mBinding.signupCPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupCPasswordTil.apply {
                setStartIconDrawable(R.drawable.check_circle_24)  // Set success icon if passwords match
                setStartIconTintList(ColorStateList.valueOf(Color.GRAY))  // Gray tint for the success icon
            }
        }

        return errorMessage == null
    }

    // Simulates checking for unique username (can be replaced with an API call)
    private fun isUniqueUsername(username: String): Boolean {
        val takenUsernames = listOf("user1", "admin", "test_user") // Simulate taken usernames
        return !takenUsernames.contains(username)
    }

    // Handles focus change events for form fields
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        when (view?.id) {
            R.id.signup_UsernameEt -> if (!hasFocus) validateUsername()
            R.id.signup_PasswordEt -> if (!hasFocus) validatePassword()
            R.id.signup_cPasswordEt -> if (!hasFocus) validateConfirmPassword()
            R.id.fullname_setupEt -> if (!hasFocus) validateFullName()
            R.id.signup_emailEt -> if (!hasFocus) validateEmail()
        }
    }

    // Placeholder for handling key press events (not used in this case)
    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }

    // Handles onClick events (can be extended with additional logic)
    override fun onClick(view: View?) {
        // Additional logic can be placed here for onClick events if needed
    }
}