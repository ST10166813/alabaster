package com.example.alabaster.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.databinding.ItemVolunteerBinding
import com.example.alabaster.model.Volunteer

class VolunteerAdapter(private val volunteers: List<Volunteer>) :
    RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder>() {

    inner class VolunteerViewHolder(val binding: ItemVolunteerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerViewHolder {
        val binding = ItemVolunteerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VolunteerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VolunteerViewHolder, position: Int) {
        val v = volunteers[position]
        holder.binding.tvName.text = "Name: ${v.name}"
        holder.binding.tvEmail.text = "Email: ${v.email}"
        holder.binding.tvPhone.text = "Phone: ${v.phone}"
        holder.binding.tvInterest.text = "Interest: ${v.areaOfInterest}"
        holder.binding.tvAvailability.text = "Availability: ${v.availability}"
        holder.binding.tvMessage.text = "Message: ${v.message}"
    }

    override fun getItemCount(): Int = volunteers.size
}
