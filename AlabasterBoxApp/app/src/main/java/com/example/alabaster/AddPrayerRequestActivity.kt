package com.example.alabaster

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPrayerRequestActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etRequest: EditText
    private lateinit var btnSubmit: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prayer_request)

        etName = findViewById(R.id.etName)
        etRequest = findViewById(R.id.etRequest)
        btnSubmit = findViewById(R.id.btnSubmit)

        dbRef = FirebaseDatabase.getInstance().getReference("prayer_requests")
        auth = FirebaseAuth.getInstance()

        btnSubmit.setOnClickListener {
            savePrayerRequest()
        }
    }

    private fun savePrayerRequest() {
        val name = etName.text.toString().trim()
        val requestText = etRequest.text.toString().trim()

        if (name.isEmpty() || requestText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val requestId = dbRef.push().key!!
        val userEmail = auth.currentUser?.email ?: "Anonymous"

        val prayerRequest = mapOf(
            "id" to requestId,
            "name" to name,
            "email" to userEmail,
            "request" to requestText,
            "status" to "Pending Prayer"
        )

        dbRef.child(requestId).setValue(prayerRequest)
            .addOnSuccessListener {
                Toast.makeText(this, "Prayer request submitted 🙏", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to submit request", Toast.LENGTH_SHORT).show()
            }
    }
}
