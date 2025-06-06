package com.example.shoplist

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoplist.data.RegisterRequest
import com.example.shoplist.network.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameField = findViewById<EditText>(R.id.etUsername)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.etConfirmPassword)
        val registerBtn = findViewById<Button>(R.id.btnRegister)

        registerBtn.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            // Text input validation
            if (username.isBlank()) {
                usernameField.error = "Podaj nazwę użytkownika"
                return@setOnClickListener
            }
            if (password.isBlank()) {
                passwordField.error = "Podaj hasło"
                return@setOnClickListener
            }
            if (confirmPassword.isBlank()) {
                confirmPasswordField.error = "Powtórz hasło"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "Hasła się nie zgadzają", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.authApi.register(RegisterRequest(username, password))
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Zarejestrowano", Toast.LENGTH_SHORT).show()
                        finish() // Go to LoginActivity
                    } else if (!response.isSuccessful) {
                        val errorBody = response.errorBody()?.string()
                        Log.e("RegisterActivity", "Rejestracja nie powiodła się: $errorBody")
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Błąd: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
