package com.example.alabaster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import android.app.DatePickerDialog
import java.util.Calendar


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Back button
        binding.imageView.setOnClickListener { finish() }

        // Login link
        binding.loginLink.setOnClickListener {
            // Go to LoginActivity
            // startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.dobInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format: dd - mm - yyyy
                    val date = String.format("%02d - %02d - %04d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.dobInput.setText(date)
                }, year, month, day
            )

            datePicker.show()
        }
        // Register button
        binding.button2.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val dob = binding.dobInput.text.toString().trim()

            if (name.isEmpty()) {
                binding.nameInput.error = "Name required"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInput.error = "Valid email required"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.passwordInput.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }
            if (dob.isEmpty()) {
                binding.dobInput.error = "Date of birth required"
                return@setOnClickListener
            }

            // Firebase Auth Register
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show()
                        // Optionally go to MainActivity
                        // startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}