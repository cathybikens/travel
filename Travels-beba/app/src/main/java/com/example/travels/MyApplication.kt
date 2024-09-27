package com.example.travels

import android.app.Application

class MyApplication : Application() {
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate() {
        super.onCreate()
        // Initialize the DatabaseHelper
        databaseHelper = DatabaseHelper(this)
    }
}
