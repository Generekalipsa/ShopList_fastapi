package com.example.shoplist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load fragment with shopping list
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ShoppingListFragment())
            .commit()

        // Floating Action Button to logout
        findViewById<FloatingActionButton>(R.id.fab_logout).setOnClickListener {
            // Remove token from shared preferences
            val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
            prefs.edit().remove("token").apply()

            // Go to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Floating Action Button to add new item
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }
}
