package com.example.blood_banker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Review(
    val reviewId: Int,
    val reviewerName: String,
    val reviewText: String,
    val rating: Float,      // Add this field
    val timestamp: String
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4) {

    companion object {
        private const val DATABASE_NAME = "BloodBanker.db"
        private const val TABLE_NAME = "users"
        private const val COL_1 = "ID"
        private const val COL_2 = "USERNAME"
        private const val COL_3 = "PASSWORD"

        private const val TABLE_REVIEWS = "reviews"
        private const val COL_REVIEW_ID = "review_id"
        private const val COL_REVIEWER_NAME = "reviewer_name"
        private const val COL_REVIEW_TEXT = "review_text"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the 'users' table
        val createUsersTable =
            "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)"
        db?.execSQL(createUsersTable)

        // Create the 'appointments' table
        val createAppointmentsTable =
            """
            CREATE TABLE appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT,
                blood_group TEXT,
                location TEXT,
                contact TEXT
            )
            """.trimIndent()
        db?.execSQL(createAppointmentsTable)

        // Create the 'emergency' table
        val createEmergencyTable =
            """
            CREATE TABLE emergency (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                details TEXT
            )
            """.trimIndent()
        db?.execSQL(createEmergencyTable)

        // Create the 'reviews' table
        val createReviewsTable = """
            CREATE TABLE $TABLE_REVIEWS (
                $COL_REVIEW_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_REVIEWER_NAME TEXT,
                $COL_REVIEW_TEXT TEXT,
                rating FLOAT,
                timestamp TEXT
            
            )
            """.trimIndent()
        db?.execSQL(createReviewsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop tables if they exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS appointments")
        db?.execSQL("DROP TABLE IF EXISTS emergency")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEWS")
        onCreate(db) // Recreate the tables
    }

    // Insert user data into the 'users' table
    fun insertData(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_2, username)  // Insert username
            put(COL_3, password)  // Insert password
        }

        // Insert the new user record into the table
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close() // Close the database connection
        return result != -1L  // Return true if the insertion was successful
    }

    // Check if a username already exists in the database
    fun isUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE USERNAME = ?", arrayOf(username))

        val exists = cursor.count > 0
        cursor.close() // Close the cursor to avoid memory leaks
        db.close() // Close the database connection
        return exists // Return true if the username exists
    }

    // Check user credentials for sign-in (username and password validation)
    fun checkUserCredentials(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE USERNAME = ? AND PASSWORD = ?",
            arrayOf(username, password)
        )

        val isValid = cursor.count > 0
        cursor.close() // Close the cursor to avoid memory leaks
        db.close() // Close the database connection
        return isValid // Return true if the credentials are valid
    }

    // Insert appointment into the 'appointments' table
    fun insertAppointment(
        fullName: String,
        bloodGroup: String,
        location: String,
        contact: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("full_name", fullName) // Insert full_name
            put("blood_group", bloodGroup) // Insert blood_group
            put("location", location) // Insert location
            put("contact", contact) // Insert contact
        }

        val result = db.insert("appointments", null, contentValues)
        db.close()
        return result != -1L // Return true if the insertion was successful
    }

    // Get all appointments from the 'appointments' table
    fun getAllAppointments(): ArrayList<Appointment> {
        val appointmentList = ArrayList<Appointment>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM appointments", null)

        if (cursor.moveToFirst()) {
            do {
                // Get the column indices
                val fullNameIndex = cursor.getColumnIndex("full_name")
                val bloodGroupIndex = cursor.getColumnIndex("blood_group")
                val locationIndex = cursor.getColumnIndex("location")
                val contactIndex = cursor.getColumnIndex("contact")
                // Ensure indices are valid (not -1)
                if (fullNameIndex != -1 && bloodGroupIndex != -1 && locationIndex != -1) {
                    val fullName = cursor.getString(fullNameIndex)
                    val bloodGroup = cursor.getString(bloodGroupIndex)
                    val location = cursor.getString(locationIndex)
                    val contact = cursor.getString(contactIndex)

                    appointmentList.add(Appointment(fullName, bloodGroup, location, contact))
                } else {
                    Log.e(
                        "DatabaseHelper",
                        "Column indices are invalid: fullNameIndex=$fullNameIndex, bloodGroupIndex=$bloodGroupIndex, locationIndex=$locationIndex, contactIndex=$contactIndex"
                    )
                }
            } while (cursor.moveToNext())
        }
        cursor.close() // Close cursor after use
        db.close() // Close the database connection
        return appointmentList
    }

    // Insert emergency details into the 'emergency' table
    fun insertEmergencyDetails(details: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("details", details)  // Use the correct column name "details"
        }

        val result =
            db.insert("emergency", null, contentValues)  // Use the correct table name "emergency"
        db.close() // Close the database connection
        return result != -1L  // Returns true if insertion is successful
    }

    // Get all emergency details from the 'emergency' table
    fun getAllEmergencyDetails(): List<String> {
        val emergencyList = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT details FROM emergency", null)

        if (cursor.moveToFirst()) {
            do {
                val detailsIndex = cursor.getColumnIndex("details")
                if (detailsIndex != -1) {
                    val details = cursor.getString(detailsIndex)
                    emergencyList.add(details)
                } else {
                    Log.e("DatabaseHelper", "Column 'details' does not exist")
                }
            } while (cursor.moveToNext())
        }
        cursor.close() // Close cursor after use
        db.close() // Close the database connection
        return emergencyList
    }

    // Insert review into the 'reviews' table
    // Insert review into the 'reviews' table
    fun insertReview(reviewerName: String, reviewText: String, rating: Float, timestamp: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_REVIEWER_NAME, reviewerName)  // Insert reviewer name
            put(COL_REVIEW_TEXT, reviewText)      // Insert review text
            put("rating", rating)                  // Insert rating
            put("timestamp", timestamp)            // Insert timestamp
        }

        val result = db.insert(TABLE_REVIEWS, null, contentValues)  // Insert the review into the reviews table
        db.close() // Close the database connection
        return result != -1L  // Return true if the insertion was successful
    }


    // Get all reviews from the 'reviews' table
    // Get all reviews from the 'reviews' table
    fun getAllReviews(): List<Review> {
        val reviewList = mutableListOf<Review>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_REVIEWS", null)

        if (cursor.moveToFirst()) {
            do {
                val reviewIdIndex = cursor.getColumnIndex(COL_REVIEW_ID)
                val reviewerNameIndex = cursor.getColumnIndex(COL_REVIEWER_NAME)
                val reviewTextIndex = cursor.getColumnIndex(COL_REVIEW_TEXT)
                val ratingIndex = cursor.getColumnIndex("rating")        // New line to get rating
                val timestampIndex = cursor.getColumnIndex("timestamp")  // New line to get timestamp

                if (reviewerNameIndex != -1 && reviewTextIndex != -1 && ratingIndex != -1 && timestampIndex != -1) {
                    val reviewId = cursor.getInt(reviewIdIndex)
                    val reviewerName = cursor.getString(reviewerNameIndex)
                    val reviewText = cursor.getString(reviewTextIndex)
                    val rating = cursor.getFloat(ratingIndex)            // Get rating from the cursor
                    val timestamp = cursor.getString(timestampIndex)      // Get timestamp from the cursor

                    reviewList.add(Review(reviewId, reviewerName, reviewText, rating, timestamp)) // Pass all values to Review
                } else {
                    Log.e("DatabaseHelper", "Invalid column index in reviews table")
                }
            } while (cursor.moveToNext())
        }
        cursor.close() // Close cursor after use
        db.close() // Close the database connection
        return reviewList

    }

}
