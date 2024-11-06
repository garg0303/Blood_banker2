package com.example.blood_banker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var imgUserPhoto: ImageView
    private lateinit var btnUploadPhoto: Button
    private lateinit var spinnerBloodGroup: Spinner
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioGroupDonation: RadioGroup
    private lateinit var radioGroupTattoo: RadioGroup
    private lateinit var radioGroupHIV: RadioGroup
    private lateinit var btnSaveProfile: Button

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        imgUserPhoto = findViewById(R.id.imgUserPhoto)
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto)
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioGroupDonation = findViewById(R.id.radioGroupDonation)
        radioGroupTattoo = findViewById(R.id.radioGroupTattoo)
        radioGroupHIV = findViewById(R.id.radioGroupHIV)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        // Setup blood group spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.blood_groups,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerBloodGroup.adapter = adapter
        }

        // Upload photo button click listener
        btnUploadPhoto.setOnClickListener {
            openGallery()
        }

        // Save profile button click listener
        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    // Function to open gallery for photo selection
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                imgUserPhoto.setImageURI(uri)  // Display the selected image
            }
        }
    }

    // Function to save profile data
    private fun saveProfile() {
        val bloodGroup = spinnerBloodGroup.selectedItem.toString()

        // Get selected gender
        val selectedGenderId = radioGroupGender.checkedRadioButtonId
        val gender = findViewById<RadioButton>(selectedGenderId)?.text.toString()

        // Get donation availability
        val selectedDonationId = radioGroupDonation.checkedRadioButtonId
        val isAvailableForDonation = findViewById<RadioButton>(selectedDonationId)?.text.toString()

        // Get tattoo question response
        val selectedTattooId = radioGroupTattoo.checkedRadioButtonId
        val hasTattoo = findViewById<RadioButton>(selectedTattooId)?.text.toString()

        // Get HIV question response
        val selectedHIVId = radioGroupHIV.checkedRadioButtonId
        val testedPositiveHIV = findViewById<RadioButton>(selectedHIVId)?.text.toString()

        // Save the collected data (e.g., to a database, file, or shared preferences)
        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()

        // Here, you can add logic to store data in a database or any storage solution you're using.
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }

}
