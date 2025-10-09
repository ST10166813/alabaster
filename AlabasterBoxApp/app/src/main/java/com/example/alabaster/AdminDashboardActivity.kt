package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityAdminDashboardBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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

        // ✅ Logout
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            GoogleSignIn.getClient(
                this,
                com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
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
