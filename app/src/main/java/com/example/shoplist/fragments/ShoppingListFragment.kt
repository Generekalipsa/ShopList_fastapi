package com.example.shoplist

import ShoppingItem
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.network.CrudApi
import com.example.shoplist.network.RetrofitClient
import kotlinx.coroutines.launch

class ShoppingListFragment : Fragment() {
    private lateinit var adapter: ShoppingListAdapter
    private var selectedItem: ShoppingItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        ShoppingListData.loadItems(requireContext())
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val prefs = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Brak tokenu – zaloguj się ponownie", Toast.LENGTH_SHORT).show()
            return
        }

        val crudApi = RetrofitClient.getCrudApiWithToken(token)

        adapter = ShoppingListAdapter(ShoppingListData.items) { item ->
            selectedItem = item
            val fragment = ItemDetailFragment.newInstance(item.name, item.quantity)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchItems(crudApi)
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return
        val crudApi = RetrofitClient.getCrudApiWithToken(token)
        fetchItems(crudApi)
    }

    private fun fetchItems(crudApi: CrudApi) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = crudApi.getItems()
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    ShoppingListData.items.clear()
                    ShoppingListData.items.addAll(items)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Błąd: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Błąd pobierania danych", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
