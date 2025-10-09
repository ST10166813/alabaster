package com.example.alabaster

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.model.Testimony
import com.google.firebase.database.*

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
