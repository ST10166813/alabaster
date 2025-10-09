package com.example.alabaster

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityAddDonationBinding
import com.example.alabaster.model.Donation
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.UUID

class AddDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDonationBinding
    private val db = FirebaseDatabase.getInstance().getReference("donations")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner setup (example donation types)
        val types = listOf("Money", "Clothes", "Food", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        binding.spinnerType.adapter = adapter

        // Date picker
        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val dateStr = "$d/${m + 1}/$y"
                binding.etDate.setText(dateStr)
            }, year, month, day)

            datePicker.show()
        }

        // Save donation
        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val type = binding.spinnerType.selectedItem.toString()
            val amount = binding.etAmount.text.toString().trim()
            val contact = binding.etDesc.text.toString().trim()
            val date = binding.etDate.text.toString().trim()

            if (name.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()
            val donation = Donation(id, name, type, amount, contact, date)

            db.child(id).setValue(donation)
                .addOnSuccessListener {
                    Toast.makeText(this, "Donation added successfully!", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun clearFields() {
        binding.etName.text.clear()
        binding.etAmount.text.clear()
        binding.etDesc.text.clear()
        binding.etDate.text.clear()
        binding.spinnerType.setSelection(0)
    }
}
