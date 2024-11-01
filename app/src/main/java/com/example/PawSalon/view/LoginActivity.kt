package com.example.PawSalon.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R
import com.example.PawSalon.databinding.ActivityLoginBinding
import com.example.PawSalon.network.ApiService
import com.example.PawSalon.network.LoginRequest
import com.example.PawSalon.network.LoginResponse
import com.example.PawSalon.view_models.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.loginUsernameEt.onFocusChangeListener = this
        mBinding.loginPasswordEt.onFocusChangeListener = this

        mBinding.loginBigBtn.setOnClickListener {
            handleLogin()
        }

        // Navigate to SignUp activity
        mBinding.signUpButton.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun handleLogin() {
        if (validateUsername() && validatePassword()) {
            val username = mBinding.loginUsernameEt.text.toString()
            val password = mBinding.loginPasswordEt.text.toString()

            // Create LoginRequest object
            val loginRequest = LoginRequest(username, password)

            // Call the login API using Retrofit
            val apiService = RetrofitInstance.getClient().create(ApiService::class.java)
            apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = loginResponse?.token

                        if (!token.isNullOrEmpty()) {
                            saveToken(token)
                            Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                            // Navigate to home or main activity
                            navigateToHome()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@LoginActivity, "Login Failed: ${errorBody}", Toast.LENGTH_SHORT).show()
                        Log.e("LoginError", "Response: ${errorBody}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginError", "onFailure: ${t.message}", t)
                    Toast.makeText(this@LoginActivity, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun saveToken(token: String) {
        // Save token in SharedPreferences for future access
        val sharedPreferences = getSharedPreferences("PawSalonPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun validateUsername(): Boolean {
        val value = mBinding.loginUsernameEt.text.toString()
        return if (value.isEmpty()) {
            mBinding.loginUsernameTil.apply {
                isErrorEnabled = true
                error = "Username is required"
            }
            false
        } else {
            mBinding.loginUsernameTil.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = mBinding.loginPasswordEt.text.toString()
        return if (value.isEmpty()) {
            mBinding.loginPasswordTil.apply {
                isErrorEnabled = true
                error = "Password is required"
            }
            false
        } else {
            mBinding.loginPasswordTil.isErrorEnabled = false
            true
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.login_UsernameEt -> if (!hasFocus) validateUsername()
                R.id.login_PasswordEt -> if (!hasFocus) validatePassword()
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.login_bigBtn -> handleLogin()
        }
    }
}
