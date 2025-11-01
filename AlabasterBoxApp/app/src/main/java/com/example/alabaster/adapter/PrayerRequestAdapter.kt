package com.example.alabaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PrayerRequestAdapter(
    private val prayerList: List<Map<String, String>>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<PrayerRequestAdapter.PrayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayer_request, parent, false)
        return PrayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PrayerViewHolder, position: Int) {
        val request = prayerList[position]
        val id = request["id"] ?: ""  // ✅ Ensure ID exists
        holder.bind(request)

        holder.btnDelete.setOnClickListener {
            onDeleteClick(id)
        }
    }

    override fun getItemCount(): Int = prayerList.size

    inner class PrayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvRequest: TextView = itemView.findViewById(R.id.tvRequest)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(request: Map<String, String>) {
            tvName.text = "From: ${request["name"]}"
            tvEmail.text = "Email: ${request["email"]}"
            tvRequest.text = request["request"]
        }
    }
}
