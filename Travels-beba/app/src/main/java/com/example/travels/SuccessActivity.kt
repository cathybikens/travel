package com.example.travels

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val ticketNumber = intent.getStringExtra("ticketNumber")
        val ticketTextView = findViewById<TextView>(R.id.ticketTextView)
        ticketTextView.text = "Booking Successful!\nYour Ticket Number: $ticketNumber"
    }
}
