package com.example.shoplist.data

data class AddItemRequest (
    val name: String,
    val quantity: Int = 0,
    val is_purchased: Boolean = false
)
