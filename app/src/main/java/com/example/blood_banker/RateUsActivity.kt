package com.example.blood_banker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RateUsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_us)

        dbHelper = DatabaseHelper(this)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val reviewText = findViewById<EditText>(R.id.etReview)
        val submitButton = findViewById<Button>(R.id.btnSubmitReview)
        val reviewDisplay = findViewById<TextView>(R.id.tvCustomerReviews)

        // Display all existing reviews
        displayReviews(reviewDisplay)

        // Handle review submission
        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val review = reviewText.text.toString().trim()

            if (review.isNotEmpty() && rating > 0) {
                // Get the current timestamp
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val success = dbHelper.insertReview("User Name", review, rating, timestamp) // Pass a reviewer name
                if (success) {
                    Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show()
                    displayReviews(reviewDisplay)
                    reviewText.text.clear()
                    ratingBar.rating = 0f
                } else {
                    Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please provide a rating and review text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayReviews(reviewDisplay: TextView) {
        val reviews = dbHelper.getAllReviews()
        val formattedReviews = StringBuilder("Reviews from our Valuable Customers:\n\n")
        for (review in reviews) {
            formattedReviews.append("Rating: ${review.rating} Stars\n")
            formattedReviews.append("Review: ${review.reviewText}\n")
            formattedReviews.append("Date: ${review.timestamp}\n\n")
        }
        reviewDisplay.text = formattedReviews.toString()
    }
}
