package com.example.shoplist

import ShoppingItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _cartItems = MutableLiveData<MutableList<ShoppingItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<ShoppingItem>> = _cartItems

    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    // Dodaj produkt do koszyka
    fun addToCart(product: ShoppingItem) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.add(product)
        _cartItems.value = currentList
    }

    // Usuń produkt z koszyka
    fun removeFromCart(product: ShoppingItem) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.remove(product)
        _cartItems.value = currentList
    }
}
