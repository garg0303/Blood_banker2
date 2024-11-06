package com.example.blood_banker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewsAdapter(private val reviewsList: List<String>): RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.tvReview.text = reviewsList[position]
    }

    override fun getItemCount(): Int = reviewsList.size

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReview: TextView = itemView.findViewById(R.id.tvReview)
    }
}