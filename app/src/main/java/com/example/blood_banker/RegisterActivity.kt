package com.example.blood_banker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var etRegisterUsername: EditText
    private lateinit var etRegisterPassword: EditText
    private lateinit var btnRegisterSubmit: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the views
        etRegisterUsername = findViewById(R.id.etRegisterUsername)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit)

        // Initialize the DatabaseHelper
        dbHelper = DatabaseHelper(this)

        btnRegisterSubmit.setOnClickListener {
            val username = etRegisterUsername.text.toString()
            val password = etRegisterPassword.text.toString()

            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                // Insert the credentials into the database
                val isInserted = dbHelper.insertData(username, password)

                if (isInserted) {
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect to sign-in screen after registration
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
