//package com.example.alabaster
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.alabaster.databinding.ActivityHomeBinding
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//
//class HomeActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Firebase Auth
//        auth = FirebaseAuth.getInstance()
//
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        // Logout button
//        binding.logoutbtn.setOnClickListener {
//            logoutUser()
//        }
//
//        // Navigate to Donation screen
//        binding.donatinbtn.setOnClickListener {
//            startActivity(Intent(this, AddDonationActivity::class.java))
//        }
//
//        binding.volbtn.setOnClickListener {
//            startActivity(Intent(this, VolunteerActivity::class.java))
//        }
//
//        binding.testimonybtn.setOnClickListener {
//            startActivity(Intent(this, TestimonyActivity::class.java))
//        }
//
//        binding.PrayerRequestsBtn.setOnClickListener {
//            startActivity(Intent(this, AddPrayerRequestActivity::class.java))
//        }
//
//        binding.viewEventsBtn.setOnClickListener {
//            startActivity(Intent(this, EventsActivity::class.java))
//        }
//
//
//
//    }
//
//    private fun logoutUser() {
//        // Firebase sign out
//        auth.signOut()
//
//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener {
//            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
//            // Redirect to login
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//    }
//}

package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val handler = Handler(Looper.getMainLooper())

    // Your rotating scriptures
    private val quotes = listOf(
        "“I can do all things through Christ who strengthens me.” – Philippians 4:13",
        "“The Lord is my shepherd; I shall not want.” – Psalm 23:1 ",
        "“Be strong and courageous... the Lord your God will be with you.” – Joshua 1:9 ",
        "“For I know the plans I have for you... hope and a future.” – Jeremiah 29:11 ",
        "“The Lord is close to the brokenhearted.” – Psalm 34:18 ",
        "“Cast all your anxiety on Him because He cares for you.” – 1 Peter 5:7 ",
        "“Trust in the Lord with all your heart.” – Proverbs 3:5 ",
        "“The Lord will fight for you; you need only to be still.” – Exodus 14:14 ",
        "“With God all things are possible.” – Matthew 19:26 ",
        "“The joy of the Lord is your strength.” – Nehemiah 8:10 "
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure logout button is above all views (fixes "blocked" taps)
        binding.logoutbtn.bringToFront()
        binding.logoutbtn.elevation = 12f

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Greet user
        binding.tvUserName.text = auth.currentUser?.displayName ?: "Faithful Friend 🌿"

        // Listeners (IDs kept as you had them)
        binding.logoutbtn.setOnClickListener { logoutUser() }

        binding.viewEventsBtn.setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
        }

        binding.PrayerRequestsBtn.setOnClickListener {
            startActivity(Intent(this, AddPrayerRequestActivity::class.java))
        }

        binding.testimonybtn.setOnClickListener {
            startActivity(Intent(this, TestimonyActivity::class.java))
        }

        binding.volbtn.setOnClickListener {
            startActivity(Intent(this, VolunteerActivity::class.java))
        }

        binding.donatinbtn.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }

        // Start the quote carousel
        startQuoteRotation(binding.quoteCarousel)
    }

    private fun startQuoteRotation(quoteTextView: TextView) {
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        val fadeIn  = AlphaAnimation(0f, 1f).apply { duration = 500 }

        handler.post(object : Runnable {
            override fun run() {
                val next = quotes[Random.nextInt(quotes.size)]
                quoteTextView.startAnimation(fadeOut)
                quoteTextView.text = next
                quoteTextView.startAnimation(fadeIn)
                handler.postDelayed(this, 6000)
            }
        })
    }

    private fun logoutUser() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
