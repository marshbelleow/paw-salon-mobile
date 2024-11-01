package com.example.PawSalon.view

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
import androidx.lifecycle.lifecycleScope
import com.example.PawSalon.R
import com.google.android.material.textfield.TextInputLayout
import com.example.PawSalon.view_models.RetrofitInstance
import com.example.PawSalon.databinding.ActivitySignupBinding
import com.example.PawSalon.network.ApiService
import com.example.PawSalon.network.SignUpRequest
import com.example.PawSalon.network.SignUpResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivitySignupBinding

    // Input filter to prevent spaces
    private val noSpacesInputFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.toString().contains(" ")) "" else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        // Apply filters and listeners
        setupFiltersAndListeners()

        // Navigate to login if already a user
        mBinding.signInButton.setOnClickListener { navigateToLogin() }

        // Register user if validation is successful
        mBinding.signupBigBtn.setOnClickListener { if (validateAllFields()) registerUser() }
    }

    private fun setupFiltersAndListeners() {
        // Apply no-spaces filter to password fields
        mBinding.signupPasswordEt.filters = arrayOf(noSpacesInputFilter)
        mBinding.signupCPasswordEt.filters = arrayOf(noSpacesInputFilter)

        // Set listeners for form fields
        mBinding.signupUsernameEt.onFocusChangeListener = this
        mBinding.signupPasswordEt.onFocusChangeListener = this
        mBinding.signupCPasswordEt.onFocusChangeListener = this
        mBinding.fullnameSetupEt.onFocusChangeListener = this
        mBinding.signupEmailEt.onFocusChangeListener = this
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    private fun validateAllFields(): Boolean {
        return validateFullName() && validateEmail() && validateUsername() &&
                validatePassword() && validatePasswordAndConfirmPassword()
    }

    private fun validateFullName(): Boolean {
        val fullName = mBinding.fullnameSetupEt.text.toString().trim()
        val errorMessage = if (fullName.isEmpty()) "Full name is required" else null
        setError(mBinding.fullnameSetupTil, errorMessage)
        return errorMessage == null
    }

    private fun validateEmail(): Boolean {
        val email = mBinding.signupEmailEt.text.toString().trim()
        val errorMessage = when {
            email.isEmpty() -> "Email is required"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Enter a valid email address"
            else -> null
        }
        setError(mBinding.signupEmailTil, errorMessage)
        return errorMessage == null
    }

    private fun validateUsername(): Boolean {
        val username = mBinding.signupUsernameEt.text.toString().trim()
        val errorMessage = when {
            username.isEmpty() -> "Username is required"
            username.length < 6 -> "Username must be at least 6 characters long"
            !username.matches(Regex("^[a-zA-Z0-9_.]+$")) -> "Username can only contain letters, numbers, underscores, and periods"
            username.startsWith("_") || username.startsWith(".") || username.endsWith("_") || username.endsWith(".") -> "Username cannot start or end with special characters"
            !isUniqueUsername(username) -> "Username is already taken"
            else -> null
        }
        setError(mBinding.signupUsernameTil, errorMessage)
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        val password = mBinding.signupPasswordEt.text.toString().trim()
        val errorMessage = when {
            password.isEmpty() -> "Password is required"
            password.length < 8 -> "Password must be at least 8 characters long"
            else -> null
        }
        setError(mBinding.signupPasswordTil, errorMessage)
        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = mBinding.signupCPasswordEt.text.toString().trim()
        val errorMessage = if (confirmPassword.isEmpty()) "Confirm Password is required" else null
        setError(mBinding.signupCPasswordTil, errorMessage)
        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        val password = mBinding.signupPasswordEt.text.toString().trim()
        val confirmPassword = mBinding.signupCPasswordEt.text.toString().trim()
        val errorMessage = if (password != confirmPassword) "Passwords do not match" else null
        setError(mBinding.signupCPasswordTil, errorMessage)

        if (errorMessage == null) {
            mBinding.signupCPasswordTil.apply {
                setStartIconDrawable(R.drawable.check_circle_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GRAY))
            }
        }
        return errorMessage == null
    }

    private fun setError(textInputLayout: TextInputLayout, errorMessage: String?) {
        textInputLayout.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }
    }

    private fun isUniqueUsername(username: String): Boolean {
        val takenUsernames = listOf("user1", "admin", "test_user")
        return !takenUsernames.contains(username)
    }

    private fun registerUser() {
        val registerRequest = SignUpRequest(
            fullname = mBinding.fullnameSetupEt.text.toString().trim(),
            username = mBinding.signupUsernameEt.text.toString().trim(),
            email = mBinding.signupEmailEt.text.toString().trim(),
            password = mBinding.signupPasswordEt.text.toString().trim()
        )
// Ah,
        RetrofitInstance.getClient().create(ApiService::class.java).signup(registerRequest)
            .enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    } else {
                        Toast.makeText(this@SignUpActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Toast.makeText(this@SignUpActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            when (view?.id) {
                R.id.signup_UsernameEt -> validateUsername()
                R.id.signup_PasswordEt -> validatePassword()
                R.id.signup_cPasswordEt -> validateConfirmPassword()
                R.id.fullname_setupEt -> validateFullName()
                R.id.signup_emailEt -> validateEmail()
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?) = false
    override fun onClick(view: View?) {}
}