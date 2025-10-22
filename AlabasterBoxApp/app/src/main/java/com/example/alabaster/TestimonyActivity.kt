package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.databinding.ActivityTestimonyBinding
import com.example.alabaster.model.Testimony
import com.google.firebase.database.*

class TestimonyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestimonyBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var testimonyList: ArrayList<Testimony>
    private lateinit var adapter: TestimonyAdapter
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestimonyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.testimonyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        testimonyList = ArrayList()
        adapter = TestimonyAdapter(testimonyList)
        recyclerView.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("testimonies")

        fetchApprovedTestimonies()

        binding.addTestimonyBtn.setOnClickListener {
            startActivity(Intent(this, AddTestimonyActivity::class.java))
        }

        // Bottom navigation
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(0, 0)
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddTestimonyActivity::class.java))
            overridePendingTransition(0, 0)
        }

        binding.navDonate.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    private fun fetchApprovedTestimonies() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testimonyList.clear()
                if (snapshot.exists()) {
                    for (testimonySnap in snapshot.children) {
                        val testimony = testimonySnap.getValue(Testimony::class.java)
                        if (testimony != null && testimony.status == "approved") {
                            testimonyList.add(testimony)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@TestimonyActivity,
                    "Failed to load testimonies: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}

