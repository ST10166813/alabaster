//package com.example.alabaster
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.alabaster.adapter.EventAdapter
//import com.example.alabaster.databinding.ActivityEventsBinding
//import com.example.alabaster.model.Event
//import com.google.firebase.database.*
//
//class EventsActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityEventsBinding
//    private lateinit var database: DatabaseReference
//    private lateinit var eventList: ArrayList<Event>
//    private lateinit var adapter: EventAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityEventsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        database = FirebaseDatabase.getInstance().getReference("Events")
//        eventList = ArrayList()
//        adapter = EventAdapter(eventList)
//
//        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this)
//        binding.eventRecyclerView.adapter = adapter
//
//        // ✅ Correct ValueEventListener usage
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                eventList.clear()
//                for (eventSnapshot in snapshot.children) {
//                    val event = eventSnapshot.getValue(Event::class.java)
//                    if (event != null) {
//                        eventList.add(event)
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle errors if needed
//            }
//        })
//    }
//}

package com.example.alabaster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alabaster.adapter.EventAdapter
import com.example.alabaster.databinding.ActivityEventsBinding
import com.example.alabaster.model.Event
import com.google.firebase.database.*

class EventsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventsBinding
    private lateinit var database: DatabaseReference
    private lateinit var eventList: ArrayList<Event>
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase setup
        database = FirebaseDatabase.getInstance().getReference("Events")
        eventList = ArrayList()
        adapter = EventAdapter(eventList)

        // Recycler setup
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.eventRecyclerView.adapter = adapter

        // Load events (Home content)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    if (event != null) {
                        eventList.add(event)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // 🔙 Back button → goes to main HomeActivity (if you have one)
        binding.back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        // 🧭 Bottom Navigation

        // HOME 
        binding.eventsLayout.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()        }

        // TESTIMONIES → TestimonyActivity
        binding.testimonyLayout.setOnClickListener {
            val intent = Intent(this, TestimonyActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        // DONATE → DonateActivity
        binding.DonateLayout.setOnClickListener {
            val intent = Intent(this, AddDonationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        // ✨ Floating Button → AddTestimonyActivity
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, TestimonyActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
