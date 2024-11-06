package com.example.blood_banker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

data class City(
    val name: String,
    val state: String
)

class DonateBloodActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var spinnerCountry: Spinner
    private lateinit var spinnerState: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var cities: List<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_blood)

        // Initialize views
        val etDonorName = findViewById<EditText>(R.id.etDonorName)
        val spinnerBloodGroup = findViewById<Spinner>(R.id.spinnerBloodGroup)
        spinnerCountry = findViewById(R.id.spinnerCountry)
        spinnerState = findViewById(R.id.spinnerState)
        spinnerCity = findViewById(R.id.spinnerCity)
        val etContact = findViewById<EditText>(R.id.etContact)
        val btnSchedule = findViewById<Button>(R.id.btnSchedule)

        // Initialize database helper
        databaseHelper = DatabaseHelper(this)

        // Set up the blood group Spinner
        val bloodGroups = resources.getStringArray(R.array.blood_groups)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodGroup.adapter = adapter

        // Set up country Spinner (fixed to India)
        val countries = arrayOf("India") // Fixed country
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countryAdapter

        // Load cities from JSON in the raw folder
        loadCities()

        // Handle schedule button click
        btnSchedule.setOnClickListener {
            val donorName = etDonorName.text.toString().trim()
            val bloodGroup = spinnerBloodGroup.selectedItem.toString()
            val location = "${spinnerCity.selectedItem}, ${spinnerState.selectedItem}, India" // Fixed country as India
            val contact = etContact.text.toString().trim()

            // Check if inputs are valid
            if (donorName.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Insert appointment into the database
            val isInserted = databaseHelper.insertAppointment(donorName, bloodGroup, location, contact)

            if (isInserted) {
                Toast.makeText(this, "Appointment Scheduled Successfully", Toast.LENGTH_SHORT).show()
                // Clear the input fields after successful insertion
                etDonorName.text.clear()
                etContact.text.clear()
            } else {
                Toast.makeText(this, "Failed to Schedule Appointment", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener for state spinner
        spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedState = spinnerState.selectedItem.toString()
                fetchCities(selectedState)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case where nothing is selected
            }
        }
    }

    private fun loadCities() {
        try {
            // Load JSON from the raw folder
            val inputStream = resources.openRawResource(R.raw.states)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.use { it.readText() }

            val gson = Gson()
            val cityListType = object : TypeToken<List<City>>() {}.type
            cities = gson.fromJson(jsonString, cityListType)

            // Log the loaded cities for debugging
            Log.d("DonateBloodActivity", "Cities Loaded: $cities")

            // Extract unique states from the cities
            val stateNames = cities.map { it.state.trim() }.distinct()
            val stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stateNames)
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerState.adapter = stateAdapter

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading cities", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCities(selectedState: String) {
        // Filter cities based on the selected state
        val cityNames = cities.filter { it.state.trim() == selectedState }.map { it.name }

        // Log the city names for debugging
        Log.d("DonateBloodActivity", "Cities for $selectedState: $cityNames")

        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityNames)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = cityAdapter
    }
}
