package com.example.travels

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DatabaseHelper", "Database created")  // Log when the database is created

        val createUserTable = """
            CREATE TABLE user (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL UNIQUE,
                role TEXT NOT NULL
            )
        """.trimIndent()

        val createVehicleTable = """
            CREATE TABLE vehicle (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                driver_id INTEGER NOT NULL,
                total_seats INTEGER NOT NULL,
                FOREIGN KEY(driver_id) REFERENCES user(id)
            )
        """.trimIndent()

        val createBookingTable = """
            CREATE TABLE booking (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                vehicle_id INTEGER NOT NULL,
                customer_id INTEGER NOT NULL,
                destination TEXT NOT NULL,
                seats_booked INTEGER NOT NULL,
                FOREIGN KEY(vehicle_id) REFERENCES vehicle(id),
                FOREIGN KEY(customer_id) REFERENCES user(id)
            )
        """.trimIndent()

        val createDestinationTable = """
            CREATE TABLE destination (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL
            )
        """.trimIndent()

        // Execute the SQL commands to create tables
        db.execSQL(createUserTable)
        db.execSQL(createVehicleTable)
        db.execSQL(createBookingTable)
        db.execSQL(createDestinationTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS vehicle")
        db.execSQL("DROP TABLE IF EXISTS booking")
        db.execSQL("DROP TABLE IF EXISTS destination")
        onCreate(db)
    }

    // Add this method to insert a new user into the database with integrity checks
    fun addUser(name: String, phone: String, role: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("phone", phone)
            put("role", role)
        }

        return try {
            // Try to insert the new user
            val newRowId = db.insertOrThrow("user", null, values)
            Log.d("DatabaseHelper", "User inserted: $newRowId")  // Log the ID of the inserted user
            newRowId // Return the ID of the inserted user
        } catch (e: SQLiteConstraintException) {
            // Handle the case where the phone number already exists
            Log.e("DatabaseHelper", "User insertion failed: ${e.message}")
            -1L // Return -1 if there was a constraint violation
        } finally {
            db.close()  // Ensure the database connection is closed
        }
    }


    // Optional: Add a method to check if a user already exists by phone
    fun userExists(phone: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user WHERE phone = ?", arrayOf(phone))
        val exists = cursor.count > 0
        cursor.close() // Close the cursor to free up resources
        return exists // Return true if user exists, false otherwise
    }

    companion object {
        private const val DATABASE_NAME = "travels.db"
        private const val DATABASE_VERSION = 1
    }
}
