package com.example.alabaster.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.AdminTestimonyAdapter
import com.example.alabaster.R
import com.example.alabaster.model.Testimony
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminApproveTestimonyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var testimonyList: ArrayList<Testimony>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_approve_testimony)

        recyclerView = findViewById(R.id.recyclerViewPending)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dbRef = FirebaseDatabase.getInstance().getReference("testimonies")
        testimonyList = ArrayList()

        fetchPendingTestimonies()
    }

    private fun fetchPendingTestimonies() {
        dbRef.orderByChild("status").equalTo("pending")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    testimonyList.clear()
                    if (snapshot.exists()) {
                        for (testimonySnap in snapshot.children) {
                            val testimony = testimonySnap.getValue(Testimony::class.java)
                            if (testimony != null) {
                                testimonyList.add(testimony)
                            }
                        }
                        recyclerView.adapter = AdminTestimonyAdapter(testimonyList, dbRef)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminApproveTestimonyActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}