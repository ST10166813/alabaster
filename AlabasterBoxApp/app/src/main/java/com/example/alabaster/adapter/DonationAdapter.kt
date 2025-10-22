package com.example.alabaster.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.databinding.ItemDonationBinding
import com.example.alabaster.model.Donation

class DonationAdapter(private val donationsByCategory: Map<String, List<Donation>>) :
    RecyclerView.Adapter<DonationAdapter.CategoryViewHolder>() {

    private val categories = donationsByCategory.keys.toList()

    inner class CategoryViewHolder(val binding: ItemDonationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val donationList = donationsByCategory[category] ?: emptyList()

        holder.binding.tvCategory.text = category
        holder.binding.tvDetails.text = donationList.joinToString("\n") {
            "- ${it.name} donated ${it.type ?: "R${it.amount}"} on ${it.date}"
        }
    }

    override fun getItemCount(): Int = categories.size
}
