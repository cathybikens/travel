package com.example.travels

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private val consumerKey = "06MoavpxUqAPSrgc3q5KFcl7ojroctxsWC8VsDlFb9kfzFG8"
    private val consumerSecret = "aLlgl7TuDJdH3n8xnUGNbQ41e2Qy7EAhGGdiAKksviWFzFmJQanG2Z3Vn2oAjzPU"
    private val shortcode = "600981"
    private val lipaNaMpesaOnlinePassword = "Gbp42KowdUXsk8fWWRGypxST6ejZb9VuHt+vW/r/79Ud14mq9qfbnCcuVXMXbqhcTRXeJdRenUTgbxQfJX5/x6+UkmnLh7BmvbbYYPcgq2+nPd3pHvH4oTK0H+EiJ5zk0iUR7AzXOl3cRJMVEQeicarQFRPeXYWXTHlCBT/mJihpTnGr6Q4UXqdS1SGxlcvKY/jQh4/YwW9HMQQXGKR9J6Dr3jEjLA+jo6zdhIihD7A9kJptgDwO5wumID8ivJ32WQIGk8ekcGc33sF3nUAoCwMeNexBDxwL0WdmGJ/eUiRKQrJ3I8cbW+nxfqAv291w3xzLeRAicqpxJ4Ghl7Eh8w==" // Replace with your password
    private val callbackUrl = "https://3911-41-90-64-80.ngrok-free.app" // Replace with your actual callback URL

    private lateinit var destination: String
    private var amount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        destination = intent.getStringExtra("destination") ?: "Default Destination"
        amount = intent.getIntExtra("price", 0).toDouble()

        initiateSTKPush(amount, "+254723846319") // Replace with the customer's phone number
    }

    private fun initiateSTKPush(amount: Double, phoneNumber: String) {
        getAccessToken { accessToken ->
            val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
            val password = Base64.encodeToString((shortcode + lipaNaMpesaOnlinePassword + timestamp).toByteArray(), Base64.NO_WRAP)

            val request = STKPushRequest(
                BusinessShortCode = shortcode,
                Password = password,
                Timestamp = timestamp,
                TransactionType = "CustomerPayBillOnline",
                Amount = amount,
                PartyA = phoneNumber,
                PartyB = shortcode,
                PhoneNumber = phoneNumber,
                CallBackURL = callbackUrl,
                AccountReference = "YourAccountReference", // Update this as needed
                TransactionDesc = "Payment for $destination"
            )

            val retrofit = Retrofit.Builder()
                .baseUrl("https://sandbox.safaricom.co.ke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(M_PesaApi::class.java)
            api.initiateSTKPush("Bearer $accessToken", request).enqueue(object : Callback<STKPushResponse> {
                override fun onResponse(call: Call<STKPushResponse>, response: retrofit2.Response<STKPushResponse>) {
                    if (response.isSuccessful) {
                        Log.d("M-Pesa", "STK Push initiated: ${response.body()}")
                        // Redirect to SuccessActivity after successful STK push initiation
                        val intent = Intent(this@PaymentActivity, SuccessActivity::class.java)
                        intent.putExtra("ticketNumber", generateTicketNumber())
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("M-Pesa", "Error initiating STK Push: ${response.errorBody()?.string()}")
                        Log.e("M-Pesa", "Response Code: ${response.code()}")
                        Log.e("M-Pesa", "Response Message: ${response.message()}")
                        // Show error message to the user (consider using a Toast or AlertDialog)
                    }
                }

                override fun onFailure(call: Call<STKPushResponse>, t: Throwable) {
                    Log.e("M-Pesa", "STK Push failed: ${t.message}")
                    // Show error message to the user (consider using a Toast or AlertDialog)
                }
            })
        }
    }

    private fun getAccessToken(callback: (String) -> Unit) {
        val client = OkHttpClient()
        val credentials = Base64.encodeToString("$consumerKey:$consumerSecret".toByteArray(), Base64.NO_WRAP)

        val request = Request.Builder()
            .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
            .addHeader("Authorization", "Basic $credentials")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("M-Pesa", "Failed to get access token: ${e.message}")
                // Handle error (you can show a Toast)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: throw IOException("Empty response body")
                    val jsonObject = JSONObject(responseBody)
                    val accessToken = jsonObject.getString("access_token")
                    callback(accessToken)
                } else {
                    Log.e("M-Pesa", "Error retrieving access token: ${response.message}")
                    // Handle error (you can show a Toast)
                }
            }
        })
    }

    private fun generateTicketNumber(): String {
        return "TICKET-" + System.currentTimeMillis()
    }

    interface M_PesaApi {
        @Headers("Content-Type: application/json")
        @POST("mpesa/stkpush/v1/processrequest")
        fun initiateSTKPush(@Header("Authorization") authorization: String, @Body request: STKPushRequest): Call<STKPushResponse>
    }

    data class STKPushRequest(
        val BusinessShortCode: String,
        val Password: String,
        val Timestamp: String,
        val TransactionType: String,
        val Amount: Double,
        val PartyA: String,
        val PartyB: String,
        val PhoneNumber: String,
        val CallBackURL: String,
        val AccountReference: String,
        val TransactionDesc: String
    )

    data class STKPushResponse(
        val MerchantRequestID: String,
        val CheckoutRequestID: String,
        val ResponseCode: String,
        val ResponseDescription: String,
        val CustomerMessage: String
    )
}
