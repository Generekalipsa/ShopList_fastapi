package com.example.shoplist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoplist.data.LoginRequest
import com.example.shoplist.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)

        val usernameField = findViewById<EditText>(R.id.etUsername)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val registerLink = findViewById<TextView>(R.id.tvRegister)

        loginBtn.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isBlank()) {
                usernameField.error = "Podaj nazwę użytkownika"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                passwordField.error = "Podaj hasło"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.authApi.login(LoginRequest(username, password))
                    if (response.isSuccessful) {
                        val newToken = response.body()?.access_token ?: ""
                        prefs.edit().putString("token", newToken).apply()
                        Toast.makeText(this@LoginActivity, "Zalogowano", Toast.LENGTH_SHORT).show()

                        // Go to MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Błędne dane logowania", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Błąd: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
