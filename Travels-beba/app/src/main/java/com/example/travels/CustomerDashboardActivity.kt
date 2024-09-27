package com.example.travels

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CustomerDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)
        setupSeats()
    }

    private fun setupSeats() {
        val seatCount = 14
        val seatLayout = findViewById<GridLayout>(R.id.seatLayout)

        for (i in 0 until seatCount) {
            val seat = ImageView(this)
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(10, 10, 10, 10)
            seat.layoutParams = params
            seat.setImageResource(R.drawable.seat_available)
            seat.setOnClickListener {
                seat.setImageResource(R.drawable.seat_booked)
                val intent = Intent(this, DestinationActivity::class.java)
                startActivity(intent)
                finish()
            }
            seatLayout.addView(seat)
        }
    }
}
