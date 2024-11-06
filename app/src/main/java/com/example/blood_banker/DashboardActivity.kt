package com.example.blood_banker

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.widget.Button
import android.widget.Toast

class DashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        // Set up ActionBarDrawerToggle with DrawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Enable ActionBar and display hamburger icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up navigation item click listener for drawer items
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.nav_donate_history -> {
                    startActivity(Intent(this, DonateHistoryActivity::class.java))
                }
                R.id.nav_rate_us -> {
                    startActivity(Intent(this, RateUsActivity::class.java))
                }
                R.id.nav_logout -> {
                    performLogout()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Set up button click listeners on the main dashboard
        val btnDonateBlood = findViewById<Button>(R.id.btnDonateBlood)
        val btnEmergency = findViewById<Button>(R.id.btnEmergency)
        val btnAppointments = findViewById<Button>(R.id.btnAppointments)

        btnDonateBlood.setOnClickListener {
            startActivity(Intent(this, DonateBloodActivity::class.java))
        }

        btnEmergency.setOnClickListener {
            startActivity(Intent(this, EmergencyActivity::class.java))
        }

        btnAppointments.setOnClickListener {
            startActivity(Intent(this, AppointmentsActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Toggle navigation drawer on hamburger icon click
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performLogout() {
        // Add logout logic here (e.g., clearing user session, redirecting to login screen)
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Close the dashboard after logout
    }
}
