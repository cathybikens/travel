package com.example.travels

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.graphics.Color // Import this for color

class RegistrationActivity : AppCompatActivity() {
    private lateinit var fullName: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var idNumber: EditText
    private lateinit var driversLicense: EditText
    private lateinit var vehicleRegDocs: ImageView
    private lateinit var photoUpload: ImageView
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var userTypeGroup: RadioGroup
    private lateinit var showPasswordToggle: CheckBox
    private var isDriver = false

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Set the background color to light blue
        window.decorView.setBackgroundColor(Color.parseColor("#ADD8E6"))

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        fullName = findViewById(R.id.fullName)
        phoneNumber = findViewById(R.id.phoneNumber)
        idNumber = findViewById(R.id.idNumber)
        driversLicense = findViewById(R.id.driversLicense)
        vehicleRegDocs = findViewById(R.id.vehicleRegDocs)
        photoUpload = findViewById(R.id.photoUpload)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        registerButton = findViewById(R.id.registerButton)
        userTypeGroup = findViewById(R.id.userTypeGroup)
        showPasswordToggle = findViewById(R.id.showPasswordToggle)

        userTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            isDriver = checkedId == R.id.driverOption
            toggleDriverFields(isDriver)
        }

        showPasswordToggle.setOnCheckedChangeListener { _, isChecked ->
            password.inputType = if (isChecked) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            confirmPassword.inputType = password.inputType
            password.setSelection(password.text.length)
            confirmPassword.setSelection(confirmPassword.text.length)
        }

        photoUpload.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        vehicleRegDocs.setOnClickListener {
            Toast.makeText(this, "Upload vehicle registration document", Toast.LENGTH_SHORT).show()
        }

        registerButton.setOnClickListener {
            if (validateFields()) {
                // Save user info to the database
                val role = if (isDriver) "driver" else "customer"
                val userId = dbHelper.addUser(
                    fullName.text.toString(),
                    phoneNumber.text.toString(),
                    role
                )

                // Log the userId for debugging
                Log.d("RegistrationActivity", "User ID: $userId")

                if (userId != -1L) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = if (isDriver) {
                        Intent(this, InspectionBookingActivity::class.java)
                    } else {
                        Intent(this, CustomerDashboardActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun toggleDriverFields(isDriver: Boolean) {
        idNumber.visibility = if (isDriver) View.VISIBLE else View.GONE
        driversLicense.visibility = if (isDriver) View.VISIBLE else View.GONE
        vehicleRegDocs.visibility = if (isDriver) View.VISIBLE else View.GONE
        photoUpload.visibility = if (isDriver) View.VISIBLE else View.GONE
    }

    private fun validateFields(): Boolean {
        if (fullName.text.isBlank() || phoneNumber.text.isBlank() || password.text.isBlank() || confirmPassword.text.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (isDriver && (idNumber.text.isBlank() || driversLicense.text.isBlank())) {
            Toast.makeText(this, "Please fill all driver fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.text.toString() != confirmPassword.text.toString()) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            photoUpload.setImageBitmap(imageBitmap)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}
