package com.example.alabaster

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.model.Testimony
import com.google.firebase.database.DatabaseReference

class AdminTestimonyAdapter(
    private val testimonyList: ArrayList<Testimony>,
    private val dbRef: DatabaseReference
) : RecyclerView.Adapter<AdminTestimonyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val testimony: TextView = itemView.findViewById(R.id.tvTestimony)
        val image: ImageView = itemView.findViewById(R.id.ivImage)
        val approveBtn: Button = itemView.findViewById(R.id.btnApprove)
        val rejectBtn: Button = itemView.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_testimony, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testimony = testimonyList[position]
        holder.name.text = testimony.name
        holder.title.text = testimony.title
        holder.date.text = testimony.date
        holder.testimony.text = testimony.testimony

        if (!testimony.imageBase64.isNullOrEmpty()) {
            val imageBytes = Base64.decode(testimony.imageBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.image.setImageBitmap(bitmap)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }

        holder.approveBtn.setOnClickListener {
            dbRef.child(testimony.id!!).child("status").setValue("approved")
            Toast.makeText(holder.itemView.context, "Testimony Approved", Toast.LENGTH_SHORT).show()
        }

        holder.rejectBtn.setOnClickListener {
            dbRef.child(testimony.id!!).child("status").setValue("rejected")
            Toast.makeText(holder.itemView.context, "Testimony Rejected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = testimonyList.size
}
