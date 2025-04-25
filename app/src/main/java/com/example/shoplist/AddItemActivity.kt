package com.example.shoplist

import ShoppingItem
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val editName = findViewById<EditText>(R.id.editItemName)
        val editQty = findViewById<EditText>(R.id.editItemQty)
        val button = findViewById<Button>(R.id.buttonSave)

        button.setOnClickListener {
            val name = editName.text.toString()
            val qty = editQty.text.toString().toIntOrNull() ?: 1

            if (name.isNotBlank()) {
                ShoppingListData.addItem(ShoppingItem(name, qty))
                Toast.makeText(this, "Produkt dodany!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Wpisz nazwę produktu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
