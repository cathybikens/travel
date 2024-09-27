package com.example.travels

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travels.ui.theme.TravelsTheme

class MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Access the database helper
        databaseHelper = (application as MyApplication).databaseHelper

        setContent {
            TravelsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF87CEEB) // Sky blue background
                ) {
                    LandingPage {
                        // On button click, start the RegistrationActivity
                        startActivity(Intent(this, RegistrationActivity::class.java))
                    }
                }
            }
        }
    }
}

@Composable
fun LandingPage(onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .background(Color(0xFF87CEEB)), // Sky blue background
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo at the top center
        Image(
            painter = painterResource(id = R.drawable.brand),
            contentDescription = "Logo",
            modifier = Modifier
                .width(300.dp) // Adjust the width as needed
                .height(300.dp)
                .padding(bottom = 20.dp) // Space between logo and button
        )

        // Register button
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Color.LightGray) // Optional border
        ) {
            Text(
                text = "SIGN UP",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    TravelsTheme {
        LandingPage {}
    }
}
