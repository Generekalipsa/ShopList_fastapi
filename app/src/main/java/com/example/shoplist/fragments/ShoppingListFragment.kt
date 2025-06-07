package com.example.shoplist

import ShoppingItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
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
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val items = RetrofitClient.crudApi.getItems()
                ShoppingListData.items.clear()
                ShoppingListData.items.addAll(items)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Błąd pobierania danych", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
