package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPrayerRequestActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etRequest: EditText
    private lateinit var btnSubmit: Button
    private lateinit var ivBack: ImageView
    private lateinit var eventsLayout: LinearLayout
    private lateinit var fabAdd: com.google.android.material.floatingactionbutton.FloatingActionButton
    private lateinit var donateLayout: LinearLayout

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prayer_request)

        // Initialize Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("prayer_requests")
        auth = FirebaseAuth.getInstance()

        // Bind views
        etName = findViewById(R.id.etName)
        etRequest = findViewById(R.id.etRequest)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivBack = findViewById(R.id.ivBack)
        eventsLayout = findViewById(R.id.eventsLayout)
        fabAdd = findViewById(R.id.fabAdd)
        donateLayout = findViewById(R.id.DonateLayout)

        // Submit button logic
        btnSubmit.setOnClickListener {
            savePrayerRequest()
        }

        // Navigation button logic
        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        eventsLayout.setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        fabAdd.setOnClickListener {
            val intent = Intent(this, TestimonyActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        donateLayout.setOnClickListener {
            val intent = Intent(this, AddDonationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
