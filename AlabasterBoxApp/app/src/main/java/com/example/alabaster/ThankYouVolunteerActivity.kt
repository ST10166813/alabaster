package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityThankYouVolunteerBinding

class ThankYouVolunteerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThankYouVolunteerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThankYouVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Redirect to home after 1 minute
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 60000) // 1 minute
    }
}
