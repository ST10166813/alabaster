package com.example.alabaster.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.databinding.EventItemBinding
import com.example.alabaster.model.Event

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val e = events[position]
        holder.binding.tvTitle.text = e.title
        holder.binding.tvDate.text = "${e.date}"
        holder.binding.tvLocation.text = "📍 ${e.location}"
        holder.binding.tvDescription.text = e.description
    }

    override fun getItemCount(): Int = events.size
}
