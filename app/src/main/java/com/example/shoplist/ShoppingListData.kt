package com.example.shoplist

import ShoppingItem

object ShoppingListData {
    val items = mutableListOf<ShoppingItem>()

    fun addItem(item: ShoppingItem) {
        items.add(item)
    }
}
