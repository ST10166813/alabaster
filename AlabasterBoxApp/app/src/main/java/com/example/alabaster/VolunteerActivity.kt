package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VolunteerActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etArea: EditText
    private lateinit var etAvailability: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSubmit: Button
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        dbRef = FirebaseDatabase.getInstance().getReference("volunteers")

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etArea = findViewById(R.id.etArea)
        etAvailability = findViewById(R.id.etAvailability)
        etMessage = findViewById(R.id.etMessage)
        btnSubmit = findViewById(R.id.btnSubmitVolunteer)
        ivBack = findViewById(R.id.ivBack)

        // Back button logic — go back to HomeActivity
        ivBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        // Submit volunteer info
        btnSubmit.setOnClickListener {
            saveVolunteerInfo()
        }
    }

    private fun saveVolunteerInfo() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val area = etArea.text.toString().trim()
        val availability = etAvailability.text.toString().trim()
        val message = etMessage.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || area.isEmpty() || availability.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val volunteerId = dbRef.push().key!!
        val volunteer = mapOf(
            "id" to volunteerId,
            "name" to name,
            "email" to email,
            "phone" to phone,
            "areaOfInterest" to area,
            "availability" to availability,
            "message" to message
        )

        dbRef.child(volunteerId).setValue(volunteer)
            .addOnSuccessListener {
                Toast.makeText(this, "Thank you for volunteering!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ThankYouVolunteerActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to submit volunteer form", Toast.LENGTH_SHORT).show()
            }
    }
}
