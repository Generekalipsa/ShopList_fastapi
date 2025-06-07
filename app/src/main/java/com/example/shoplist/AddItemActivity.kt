package com.example.shoplist

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shoplist.data.AddItemRequest
import com.example.shoplist.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val editName = findViewById<EditText>(R.id.editItemName)
        val editQty = findViewById<EditText>(R.id.editItemQty)
        val button = findViewById<Button>(R.id.buttonSave)

        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        if (token == null) {
            Toast.makeText(this, "Brak tokenu – zaloguj się ponownie", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val crudApi = RetrofitClient.getCrudApiWithToken(token)

        button.setOnClickListener {
            val name = editName.text.toString()
            val qty = editQty.text.toString().toIntOrNull() ?: 1

            if (name.isNotBlank()) {
                val request = AddItemRequest(name, qty)

                lifecycleScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            crudApi.addItem(request)
                        }

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddItemActivity,
                                "Produkt dodany!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddItemActivity,
                                "Błąd serwera: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@AddItemActivity,
                            "Błąd: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "Wpisz nazwę produktu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
