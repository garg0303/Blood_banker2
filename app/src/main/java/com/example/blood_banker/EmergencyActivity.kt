package com.example.blood_banker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EmergencyActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        databaseHelper = DatabaseHelper(this)

        // Initialize the spinner for blood group
        val spinnerBloodGroup = findViewById<Spinner>(R.id.spinnerBloodGroup)
        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodGroup.adapter = adapter

        val etlocation = findViewById<EditText>(R.id.etLocation)
        val etcontactinfo = findViewById<EditText>(R.id.etContactInfo)
        val btnSendEmergency = findViewById<Button>(R.id.btnSendEmergency)

        btnSendEmergency.setOnClickListener {
            val bloodGroup = spinnerBloodGroup.selectedItem.toString()
            val location = etlocation.text.toString().trim()
            val contactInfo = etcontactinfo.text.toString().trim()

            // Validate input fields
            if (location.isEmpty() || contactInfo.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Combine details into a single string
            val details = "Blood Group: $bloodGroup, Location: $location, Contact: $contactInfo"

            // Save details to the database
            val isInserted = databaseHelper.insertEmergencyDetails(details)

            if (isInserted) {
                Toast.makeText(this, "Emergency Request Sent!", Toast.LENGTH_SHORT).show()
                etlocation.text.clear()
                etcontactinfo.text.clear()

                // Start EmergencyDetailsActivity and pass the details
                val intent = Intent(this, EmergencyDetailsActivity::class.java).apply {
                    putExtra("EMERGENCY_DETAILS", details)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to send emergency request", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
