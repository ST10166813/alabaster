package com.example.alabaster.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alabaster.R
import com.example.alabaster.adapter.VolunteerAdapter
import com.example.alabaster.databinding.ActivityAdminVolunteerListBinding
import com.example.alabaster.model.Volunteer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminVolunteerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminVolunteerListBinding
    private lateinit var database: DatabaseReference
    private lateinit var volunteerList: ArrayList<Volunteer>
    private lateinit var adapter: VolunteerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminVolunteerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("volunteers")
        volunteerList = ArrayList()
        adapter = VolunteerAdapter(volunteerList)
        binding.recyclerViewVolunteers.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVolunteers.adapter = adapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                volunteerList.clear()
                for (volunteerSnapshot in snapshot.children) {
                    val volunteer = volunteerSnapshot.getValue(Volunteer::class.java)
                    if (volunteer != null) volunteerList.add(volunteer)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish() // close current screen so it doesn’t stack
        }
    }
}