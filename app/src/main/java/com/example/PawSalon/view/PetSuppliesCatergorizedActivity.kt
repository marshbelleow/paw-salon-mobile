package com.example.PawSalon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R

class PetSuppliesCatergorizedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petsuppliescategorized)

        val petshampooButton: Button = findViewById(R.id.btn_petshampoo)
        val petpadsButton: Button = findViewById(R.id.btn_petpads)
        val dogfoodButton: Button = findViewById(R.id.btn_dogfood)
        val petbowlButton: Button = findViewById(R.id.btn_petbowl)
        val catfoodButton: Button = findViewById(R.id.btn_catfood)
        val dogleashButton: Button = findViewById(R.id.btn_dogleash)
        val petcologneButton: Button = findViewById(R.id.btn_petcologne)
        val petcareproductButton: Button = findViewById(R.id.btn_petcareproducts)
        val pettoysButton: Button = findViewById(R.id.btn_pettoys)
        val petlitterButton: Button = findViewById(R.id.btn_petlitter)

        petlitterButton.setOnClickListener {
            val intent = Intent(this, PetLitterActivity::class.java)
            startActivity(intent)
        }

       pettoysButton.setOnClickListener {
            val intent = Intent(this, PetToysActivity::class.java)
            startActivity(intent)
        }

        petcareproductButton.setOnClickListener {
            val intent = Intent(this, PetCareProductActivity::class.java)
            startActivity(intent)
        }

        petcologneButton.setOnClickListener {
            val intent = Intent(this, PetCologneActivity::class.java)
            startActivity(intent)
        }

        dogleashButton.setOnClickListener {
            val intent = Intent(this, DogLeashActivity::class.java)
            startActivity(intent)
        }

        catfoodButton.setOnClickListener {
            val intent = Intent(this, CatFoodActivity::class.java)
            startActivity(intent)
        }

        petbowlButton.setOnClickListener {
            val intent = Intent(this, PetBowlActivity::class.java)
            startActivity(intent)
        }



        dogfoodButton.setOnClickListener {
            val intent = Intent(this, DogFoodActivity::class.java)
            startActivity(intent)
        }


        petpadsButton.setOnClickListener {
            val intent = Intent(this, PetPadsActivity::class.java)
            startActivity(intent)
        }


        petshampooButton.setOnClickListener {
            val intent = Intent(this, PetShampooActivity::class.java)
            startActivity(intent)
        }


        // Settings button navigation
        val settingsImageButton: ImageButton = findViewById(R.id.btn_settings)
        settingsImageButton.setOnClickListener {
            // Navigate directly to SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Feedback button navigation
        val feedbackImageButton: ImageButton = findViewById(R.id.btn_feedback)
        feedbackImageButton.setOnClickListener {
            // Navigate directly to FeedbackActivity
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        // Appointment button navigation
        val appointmentImageButton: ImageButton = findViewById(R.id.btn_appointment)
        appointmentImageButton.setOnClickListener {
            // Navigate directly to AppointmentActivity
            val intent = Intent(this, ServiceAppointmentActivity::class.java)
            startActivity(intent)
        }

        // Home button navigation
        val homeButton: ImageButton = findViewById(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
