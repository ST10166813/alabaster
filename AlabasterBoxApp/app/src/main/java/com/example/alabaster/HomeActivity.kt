package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Logout button
        binding.logoutbtn.setOnClickListener {
            logoutUser()
        }

        // Navigate to Donation screen
        binding.donatinbtn.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }

        binding.volbtn.setOnClickListener {
            startActivity(Intent(this, VolunteerActivity::class.java))
        }

        binding.testimonybtn.setOnClickListener {
            startActivity(Intent(this, TestimonyActivity::class.java))
        }

        binding.PrayerRequestsBtn.setOnClickListener {
            startActivity(Intent(this, AddPrayerRequestActivity::class.java))
        }

    }

    private fun logoutUser() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            // Redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
