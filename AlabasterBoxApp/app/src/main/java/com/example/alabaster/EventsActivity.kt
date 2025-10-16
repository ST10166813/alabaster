package com.example.alabaster

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

        database = FirebaseDatabase.getInstance().getReference("Events")
        eventList = ArrayList()
        adapter = EventAdapter(eventList)

        binding.eventRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.eventRecyclerView.adapter = adapter

        // ✅ Correct ValueEventListener usage
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

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}
