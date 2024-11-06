package com.example.blood_banker

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AppointmentsActivity : AppCompatActivity() {
    private lateinit var appointmentsListView: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var appointmentList: ArrayList<Appointment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        appointmentsListView = findViewById(R.id.appointmentsListView)
        databaseHelper = DatabaseHelper(this)

        loadAppointments()
    }

    private fun loadAppointments() {
        appointmentList = databaseHelper.getAllAppointments() // Fetch appointments from the database
        Log.d("AppointmentsActivity", "Fetched appointments: $appointmentList") // Log fetched appointments

        if (appointmentList.isEmpty()) {
            Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = AppointmentAdapter(this, appointmentList)
            appointmentsListView.adapter = adapter
        }
    }
}
