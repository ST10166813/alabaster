//package com.example.alabaster.admin
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.alabaster.admin.AdminPrayerRequestsActivity
//import com.example.alabaster.admin.AdminVolunteerListActivity
//import com.example.alabaster.LoginActivity
//import com.example.alabaster.databinding.ActivityAdminDashboardBinding
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class AdminDashboardActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityAdminDashboardBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var dbRef: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
//        dbRef = FirebaseDatabase.getInstance().getReference("testimonies")
//
//        // ✅ Load Pending Testimony Count
//        fetchPendingTestimonyCount()
//
//        // ✅ Go to Testimony Approval Screen
//        binding.manageTestimoniesBtn.setOnClickListener {
//            val intent = Intent(this, AdminApproveTestimonyActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.viewPrayerRequestsBtn.setOnClickListener {
//            startActivity(Intent(this, AdminPrayerRequestsActivity::class.java))
//        }
//
//        binding.volviewBtn.setOnClickListener {
//            startActivity(Intent(this, AdminVolunteerListActivity::class.java))
//        }
//
//        binding.addEventBtn.setOnClickListener {
//            startActivity(Intent(this, AdminAddEventActivity::class.java))
//        }
//
//        binding.btndon.setOnClickListener {
//            startActivity(Intent(this, AdminDonationsActivity::class.java))
//        }
//
//        // ✅ Logout
//        binding.logoutBtn.setOnClickListener {
//            auth.signOut()
//            GoogleSignIn.getClient(
//                this,
//                GoogleSignInOptions.DEFAULT_SIGN_IN
//            ).signOut()
//
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//    }
//
//    private fun fetchPendingTestimonyCount() {
//        dbRef.orderByChild("status").equalTo("Pending")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val pendingCount = snapshot.childrenCount.toInt()
//                    binding.manageTestimoniesBtn.text =
//                        "Manage Testimonies (${pendingCount} pending)"
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    binding.manageTestimoniesBtn.text = "Manage Testimonies"
//                }
//            })
//    }
//}

package com.example.alabaster.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityAdminDashboardBinding
import com.google.firebase.database.*

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var db: DatabaseReference

    private var testimoniesListener: ValueEventListener? = null
    private var prayerListener: ValueEventListener? = null
    private var donationsListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference

        // ---- Clicks on the cards (use the CardView itself) ----
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
        binding.logoutBtn.setOnClickListener {
            // your logout logic here
            finish()
        }

        // ---- Live counts for cards (update the INNER TextViews, not the cards) ----
        attachTestimonyPendingCount()
        attachPrayerCount()
        attachDonationsCount()
    }

    private fun attachTestimonyPendingCount() {
        // Example: Testimonies with status = "pending"
        val ref = db.child("Testimonies")
        testimoniesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pending = snapshot.children.count {
                    (it.child("status").getValue(String::class.java) ?: "").equals("pending", ignoreCase = true)
                }
                binding.tvManageTestimoniesLabel.text = "Manage Testimonies ($pending pending)"
            }
            override fun onCancelled(error: DatabaseError) {
                binding.tvManageTestimoniesLabel.text = "Manage Testimonies"
            }
        }
        ref.addValueEventListener(testimoniesListener as ValueEventListener)
        // If you have a large dataset, you can use a query:
        // ref.orderByChild("status").equalTo("pending") ...
    }

    private fun attachPrayerCount() {
        // Example: all prayer requests count (or filter by open status if you store one)
        val ref = db.child("PrayerRequests")
        prayerListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val openCount = snapshot.children.count {
                    // If you track status: (it.child("status").value == "open")
                    true // count all if no status
                }
                binding.tvPrayerLabel.text = "View Prayer Requests ($openCount)"
            }
            override fun onCancelled(error: DatabaseError) {
                binding.tvPrayerLabel.text = "View Prayer Requests"
            }
        }
        ref.addValueEventListener(prayerListener as ValueEventListener)
    }

    private fun attachDonationsCount() {
        val ref = db.child("Donations")
        donationsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                binding.tvDonationsLabel.text = "View Donations ($count)"
            }
            override fun onCancelled(error: DatabaseError) {
                binding.tvDonationsLabel.text = "View Donations"
            }
        }
        ref.addValueEventListener(donationsListener as ValueEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        testimoniesListener?.let { db.child("Testimonies").removeEventListener(it) }
        prayerListener?.let { db.child("PrayerRequests").removeEventListener(it) }
        donationsListener?.let { db.child("Donations").removeEventListener(it) }
    }
}
