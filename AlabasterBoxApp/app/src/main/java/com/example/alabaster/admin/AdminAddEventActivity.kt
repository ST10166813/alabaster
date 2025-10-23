package com.example.alabaster.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityAdminAddEventBinding
import com.example.alabaster.model.Event
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class AdminAddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddEventBinding
    private lateinit var database: FirebaseDatabase
    private val dateFmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        // Entry animation for the column (from XML layoutAnimation)
        binding.contentColumn.layoutAnimation?.let { binding.contentColumn.startLayoutAnimation() }

        // Subtle focus animations for inputs
        setFocusAnim(binding.etTitle, binding.tilTitle)
        setFocusAnim(binding.etDate, binding.tilDate)
        setFocusAnim(binding.etLocation, binding.tilLocation)
        setFocusAnim(binding.etDescription, binding.tilDescription)

        // Material DatePicker for etDate
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Select Event Date")
            .build()

        binding.tilDate.setEndIconOnClickListener { datePicker.show(supportFragmentManager, "date") }
        binding.etDate.setOnClickListener { datePicker.show(supportFragmentManager, "date") }

        datePicker.addOnPositiveButtonClickListener { millis ->
            binding.etDate.setText(dateFmt.format(Date(millis)))
        }

        // Save to Firebase
        binding.btnAddEvent.setOnClickListener {
            val title = binding.etTitle.text?.toString()?.trim().orEmpty()
            val date = binding.etDate.text?.toString()?.trim().orEmpty()
            val location = binding.etLocation.text?.toString()?.trim().orEmpty()
            val description = binding.etDescription.text?.toString()?.trim().orEmpty()

            if (title.isEmpty() || date.isEmpty() || location.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val eventsRef = database.reference.child("Events")
            val eventId = eventsRef.push().key ?: run {
                Toast.makeText(this, "Error creating event id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val eventData = Event(eventId, title, date, location, description)

            eventsRef.child(eventId)
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

    /** Small focus animation for each TextInputLayout */
    private fun setFocusAnim(edit: View, til: com.google.android.material.textfield.TextInputLayout) {
        edit.setOnFocusChangeListener { _, hasFocus ->
            val targetScale = if (hasFocus) 1.02f else 1.0f
            val targetElevation = if (hasFocus) 8f else 0f
            til.animate().scaleX(targetScale).scaleY(targetScale).setDuration(140).start()
            (til.parent as? View)?.elevation = targetElevation
        }
    }
}
