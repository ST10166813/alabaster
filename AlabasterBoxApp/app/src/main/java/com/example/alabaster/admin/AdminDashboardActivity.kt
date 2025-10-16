package com.example.alabaster.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.admin.AdminPrayerRequestsActivity
import com.example.alabaster.admin.AdminVolunteerListActivity
import com.example.alabaster.LoginActivity
import com.example.alabaster.databinding.ActivityAdminDashboardBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("testimonies")

        // ✅ Load Pending Testimony Count
        fetchPendingTestimonyCount()

        // ✅ Go to Testimony Approval Screen
        binding.manageTestimoniesBtn.setOnClickListener {
            val intent = Intent(this, AdminApproveTestimonyActivity::class.java)
            startActivity(intent)
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

        // ✅ Logout
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchPendingTestimonyCount() {
        dbRef.orderByChild("status").equalTo("Pending")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pendingCount = snapshot.childrenCount.toInt()
                    binding.manageTestimoniesBtn.text =
                        "Manage Testimonies (${pendingCount} pending)"
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.manageTestimoniesBtn.text = "Manage Testimonies"
                }
            })
    }
}