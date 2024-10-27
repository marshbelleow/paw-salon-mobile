package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import com.example.PawSalon.R

class LoginSignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        // Apply window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find buttons by their ID
        val loginButton: Button = findViewById(R.id.btn_Login)
        val signupButton: Button = findViewById(R.id.btn_Signup)
        val guestButton: Button = findViewById(R.id.guest)

        // Set click listeners for buttons
        loginButton.setOnClickListener {
            navigateToLogin()
        }

        signupButton.setOnClickListener {
            navigateToSignup()
        }

        // Set click listener for guest button to navigate to HomeActivity
        guestButton.setOnClickListener {
            navigateToHome()
        }
    }

    // Function to navigate to LoginActivity
    private fun navigateToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    // Function to navigate to SignUpActivity
    private fun navigateToSignup() {
        val signupIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signupIntent)
    }

    // Function to navigate to HomeActivity (Guest mode)
    private fun navigateToHome() {
        val homeIntent = Intent(this, HomeScreenActivity::class.java)
        startActivity(homeIntent)
    }
}
