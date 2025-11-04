package com.example.alabaster.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.LoginActivity
import com.example.alabaster.databinding.ActivityAdminDashboardBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // ✅ Navigation Buttons
        binding.manageTestimoniesBtn.setOnClickListener {
            startActivity(Intent(this, AdminApproveTestimonyActivity::class.java))
        }

        binding.viewPrayerRequestsBtn.setOnClickListener {
            startActivity(Intent(this, AdminPrayerRequestsActivity::class.java))
        }

        binding.volviewBtn.setOnClickListener {
            startActivity(Intent(this, AdminVolunteerListActivity::class.java))
        }

        binding.addEventBtn.setOnClickListener {
            startActivity(Intent(this, AdminAddEventActivity::class.java))
        }

        binding.btndon.setOnClickListener {
            startActivity(Intent(this, AdminDonationsActivity::class.java))
        }

        // ✅ Logout
        binding.logoutBtn.setOnClickListener {
            auth.signOut()

            GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}