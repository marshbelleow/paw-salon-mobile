package com.example.PawSalon.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.PawSalon.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_pawsalon) // Ensure this matches your actual layout file
    }

    // Function to open Google Maps
    fun openLocation(view: android.view.View) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=234+Bonuan+Gueset,+Dagupan+City,+Pangasinan")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    // Function to open Phone
    fun openPhone(view: android.view.View) {
        val phoneNumber = "09238195589" // Make sure to remove any formatting
        val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (phoneIntent.resolveActivity(packageManager) != null) {
            startActivity(phoneIntent)
        } else {
            // Optionally handle the case where no dialer app is available
        }
    }

        // Function to open the email app with the predefined address and subject
    fun openEmail(view: View) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf("dagupanpawsalon@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Inquiry")
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Choose Email Client"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to open the Facebook page in the browser
    fun openFacebook(view: View) {
        val facebookUrl = "https://www.facebook.com/PawSalonDagupan"
        val facebookIntent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
        try {
            startActivity(facebookIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No browser found to open the link.", Toast.LENGTH_SHORT).show()
        }
    }
}
