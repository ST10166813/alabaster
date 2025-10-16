package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.admin.AdminDashboardActivity
import com.example.alabaster.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // Define your admin email here
    private val adminEmail = "admin@alabaster.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Email + Password login
        binding.button.setOnClickListener {
            val email = binding.editTextText.text.toString().trim()
            val password = binding.editTextText2.text.toString().trim()

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextText.error = "Enter a valid email"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.editTextText2.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            if (currentUser.email == adminEmail) {
                                // Go to Admin Dashboard
                                Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, AdminDashboardActivity::class.java))
                            } else {
                                // Go to User Home
                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                            }
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.message ?: "Login failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Forgot Password
        binding.textView5.setOnClickListener {
            val email = binding.editTextText.text.toString().trim()
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Google Sign-In
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // Google Sign-In Launcher
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { firebaseTask ->
                        if (firebaseTask.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null) {
                                if (user.email == adminEmail) {
                                    Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                                } else {
                                    Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                firebaseTask.exception?.message ?: "Google Sign-In failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } catch (e: Exception) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
}
