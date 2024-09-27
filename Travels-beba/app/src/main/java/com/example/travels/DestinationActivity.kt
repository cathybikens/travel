package com.example.travels

import android.content.Intent
import android.graphics.Color // Import this for color
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class DestinationActivity : AppCompatActivity() {

    private lateinit var nairobiEldoret: RadioButton
    private lateinit var nairobiKisumu: RadioButton
    private lateinit var eldoretNakuru: RadioButton
    private lateinit var kisumuKisii: RadioButton
    private lateinit var proceedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)

        // Set the background color to light blue
        window.decorView.setBackgroundColor(Color.parseColor("#ADD8E6"))

        // Initialize radio buttons and the proceed button
        nairobiEldoret = findViewById(R.id.nairobiEldoret)
        nairobiKisumu = findViewById(R.id.nairobiKisumu)
        eldoretNakuru = findViewById(R.id.eldoretNakuru)
        kisumuKisii = findViewById(R.id.kisumuKisii)
        proceedButton = findViewById(R.id.proceedButton)

        // Set an onClick listener for the proceed button
        proceedButton.setOnClickListener {
            // Determine the selected destination
            val destination: String = when {
                nairobiEldoret.isChecked -> "Nairobi - Eldoret"
                nairobiKisumu.isChecked -> "Nairobi - Kisumu"
                eldoretNakuru.isChecked -> "Eldoret - Nakuru"
                kisumuKisii.isChecked -> "Kisumu - Kisii"
                else -> ""
            }
            // Determine the price based on the selected destination
            val price: Int = when (destination) {
                "Nairobi - Eldoret" -> 1100
                "Nairobi - Kisumu" -> 1400
                "Eldoret - Nakuru" -> 500
                "Kisumu - Kisii" -> 1000
                else -> 0
            }
            // Create an intent to start the PaymentActivity
            val intent = Intent(this, PaymentActivity::class.java)
            // Pass the destination and price as extras
            intent.putExtra("destination", destination)
            intent.putExtra("price", price)
            // Start the PaymentActivity
            startActivity(intent)
            // Finish the current activity
            finish()
        }
    }
}
