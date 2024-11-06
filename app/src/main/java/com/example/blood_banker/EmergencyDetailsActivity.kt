package com.example.blood_banker

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmergencyDetailsActivity : AppCompatActivity() {
    private lateinit var emergencyListView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var emergencyList: List<Emergency>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_details)

        emergencyListView = findViewById(R.id.emergencyListView)
        databaseHelper = DatabaseHelper(this)

        loadEmergencyDetails()
    }

    private fun loadEmergencyDetails() {
        emergencyList = databaseHelper.getAllEmergencyDetails().map { Emergency(it) } // Map details to Emergency objects

        if (emergencyList.isEmpty()) {
            Toast.makeText(this, "No emergency requests found", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = EmergencyAdapter(this, emergencyList)
            emergencyListView.adapter = adapter
        }
    }
}
