package com.example.shoplist

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ItemDetailFragment : Fragment() {
    companion object {
        fun newInstance(name: String, quantity: Int) = ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putString("name", name)
                putInt("quantity", quantity)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = TextView(requireContext()).apply {
        val name = arguments?.getString("name") ?: ""
        val qty = arguments?.getInt("quantity") ?: 0
        text = "Produkt: $name\nIlość: $qty"
        textSize = 20f
        gravity = Gravity.CENTER
    }
}
