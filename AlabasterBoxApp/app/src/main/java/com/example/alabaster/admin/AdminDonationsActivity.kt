package com.example.alabaster.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alabaster.adapter.DonationAdapter
import com.example.alabaster.databinding.ActivityAdminDonationsBinding
import com.example.alabaster.model.Donation
import com.google.firebase.database.*

class AdminDonationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDonationsBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDonationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("donations")

        binding.rvDonations.layoutManager = LinearLayoutManager(this)
        loadDonations()
    }

    private fun loadDonations() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val donations = mutableListOf<Donation>()
                for (donationSnap in snapshot.children) {
                    donationSnap.getValue(Donation::class.java)?.let { donations.add(it) }
                }

                // Group by category
                val grouped = donations.groupBy { it.type ?: "Uncategorized" }

                binding.rvDonations.adapter = DonationAdapter(grouped)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
