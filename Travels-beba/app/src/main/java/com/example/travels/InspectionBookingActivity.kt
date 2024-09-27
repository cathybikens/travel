package com.example.travels

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InspectionBookingActivity : AppCompatActivity() {

    private lateinit var uploadVehicleRegDocsButton: Button
    private lateinit var bookInspectionButton: Button
    private lateinit var vehicleRegDocsImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection_booking)

        uploadVehicleRegDocsButton = findViewById(R.id.uploadVehicleRegDocsButton)
        bookInspectionButton = findViewById(R.id.bookInspectionButton)
        vehicleRegDocsImageView = findViewById(R.id.vehicleRegDocsImageView)

        uploadVehicleRegDocsButton.setOnClickListener {
            // Logic to upload vehicle registration document (e.g., select from gallery)
            Toast.makeText(this, "Upload vehicle registration document", Toast.LENGTH_SHORT).show()
        }

        bookInspectionButton.setOnClickListener {
            // Logic to book inspection (e.g., show date picker dialog)
            Toast.makeText(this, "Inspection booked successfully", Toast.LENGTH_SHORT).show()
            // Navigate to DriverDashboardActivity after booking
            val intent = Intent(this, DriverDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
