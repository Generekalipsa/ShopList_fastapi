package com.example.shoplist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        if (!token.isNullOrEmpty()) {
            // Token is valid - go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Token is null or empty - go to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}
