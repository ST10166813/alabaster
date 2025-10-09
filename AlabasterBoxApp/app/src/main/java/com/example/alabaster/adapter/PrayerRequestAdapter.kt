package com.example.alabaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PrayerRequestAdapter(private val prayerList: List<Map<String, String>>) :
    RecyclerView.Adapter<PrayerRequestAdapter.PrayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayer_request, parent, false)
        return PrayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PrayerViewHolder, position: Int) {
        val request = prayerList[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = prayerList.size

    class PrayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        private val tvRequest: TextView = itemView.findViewById(R.id.tvRequest)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(request: Map<String, String>) {
            tvName.text = "From: ${request["name"]}"
            tvEmail.text = "Email: ${request["email"]}"
            tvRequest.text = request["request"]
            tvStatus.text = "Status: ${request["status"]}"
        }
    }
}
