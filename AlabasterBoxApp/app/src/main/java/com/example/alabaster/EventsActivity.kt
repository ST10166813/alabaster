package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alabaster.databinding.ActivityEventsBinding

class EventsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eventsLayout.setOnClickListener {
            startActivity(Intent(this, TestimonyActivity::class.java))
        }

        binding.TestLayout.setOnClickListener {
            startActivity(Intent(this, AddTestimonyActivity::class.java))
        }

        binding.DonateLayout.setOnClickListener {
            startActivity(Intent(this, AddDonationActivity::class.java))
        }

    }
}