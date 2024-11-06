package com.example.blood_banker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnViewEmergencies = findViewById<Button>(R.id.btnViewEmergencies) // Reference to the new button

        btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for the View Emergencies button
        btnViewEmergencies.setOnClickListener {
            val intent = Intent(this, EmergencyDetailsActivity::class.java)
            startActivity(intent)
        }

    }
}
