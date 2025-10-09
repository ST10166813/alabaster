package com.example.alabaster

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alabaster.model.Testimony

class TestimonyAdapter(private val testimonyList: List<Testimony>) :
    RecyclerView.Adapter<TestimonyAdapter.TestimonyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestimonyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_testimony, parent, false)
        return TestimonyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TestimonyViewHolder, position: Int) {
        val testimony = testimonyList[position]
        holder.bind(testimony)
    }

    override fun getItemCount(): Int = testimonyList.size

    class TestimonyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTestimony: TextView = itemView.findViewById(R.id.tvTestimony)
        private val ivImage: ImageView = itemView.findViewById(R.id.ivImage)

        fun bind(testimony: Testimony) {
            tvTitle.text = testimony.title
            tvName.text = testimony.name
            tvDate.text = testimony.date
            tvTestimony.text = testimony.testimony

            if (!testimony.imageBase64.isNullOrEmpty()) {
                val imageBytes = Base64.decode(testimony.imageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ivImage.setImageBitmap(bitmap)
                ivImage.visibility = View.VISIBLE
            } else {
                ivImage.visibility = View.GONE
            }
        }
    }
}
