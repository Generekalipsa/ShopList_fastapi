package com.example.shoplist

import ShoppingItem
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ShoppingListStorage {
    private const val PREFS_NAME = "ShoppingListPrefs"
    private const val LIST_KEY = "shoppingList"

    fun save(context: Context, list: List<ShoppingItem>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(list)
        editor.putString(LIST_KEY, json)
        editor.apply()
    }

    fun load(context: Context): MutableList<ShoppingItem> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(LIST_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<ShoppingItem>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
