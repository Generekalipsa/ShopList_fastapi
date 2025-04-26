package com.example.shoplist

import ShoppingItem
import android.content.Context

object ShoppingListData {
    var items: MutableList<ShoppingItem> = mutableListOf()

    fun saveIsChecked(item: ShoppingItem, context: Context) {
        item.isChecked = !item.isChecked
        ShoppingListStorage.save(context, items)
    }

    fun addItem(item: ShoppingItem, context: Context) {
        items.add(item)
        ShoppingListStorage.save(context, items)
    }

    fun removeItem(item: ShoppingItem, context: Context) {
        items.remove(item)
        ShoppingListStorage.save(context, items)
    }

    fun loadItems(context: Context) {
        items = ShoppingListStorage.load(context)
    }
}
