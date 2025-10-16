package com.example.alabaster.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityAdminAddEventBinding
import com.example.alabaster.model.Event
import com.google.firebase.database.FirebaseDatabase

class AdminAddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddEventBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        binding.btnAddEvent.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val date = binding.etDate.text.toString().trim()
            val location = binding.etLocation.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty() || date.isEmpty() || location.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val eventId = database.reference.push().key!!
            val eventData = Event(eventId, title, date, location, description)

            database.reference.child("Events").child(eventId)
                .setValue(eventData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Event added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show()
                }
        }
    }
}