package com.example.alabaster.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.PrayerRequestAdapter
import com.example.alabaster.databinding.ActivityAdminPrayerRequestsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPrayerRequestsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPrayerRequestsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var prayerList: ArrayList<Map<String, String>>
    private lateinit var dbRef: DatabaseReference
    private lateinit var adapter: PrayerRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPrayerRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.prayerRequestsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        prayerList = ArrayList()
        adapter = PrayerRequestAdapter(prayerList)
        recyclerView.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("prayer_requests")

        fetchRequests()
    }

    private fun fetchRequests() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prayerList.clear()
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val request = snap.value as? Map<String, String>
                        if (request != null) {
                            prayerList.add(request)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminPrayerRequestsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}