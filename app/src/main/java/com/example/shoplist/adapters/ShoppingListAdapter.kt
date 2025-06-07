package com.example.shoplist

import ShoppingItem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.data.UpdateItemRequest
import com.example.shoplist.network.RetrofitClient
import kotlinx.coroutines.launch

class ShoppingListAdapter(
    private val items: MutableList<ShoppingItem>,
    private val activity: FragmentActivity,
    private val onItemClick: (ShoppingItem) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.textItemName)
        private val qtyView: TextView = itemView.findViewById(R.id.textItemQty)
        private val purchasedCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(item: ShoppingItem) {
            nameView.text = item.name
            qtyView.text = "Ilość: ${item.quantity}"
            purchasedCheckBox.setOnCheckedChangeListener(null)
            purchasedCheckBox.isChecked = item.is_purchased

            purchasedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (item.is_purchased != isChecked) {
                    updateItemPurchased(item, isChecked)
                }
            }

            deleteButton.setOnClickListener {
                deleteItem(item)
            }
        }

        private fun updateItemPurchased(item: ShoppingItem, isPurchased: Boolean) {
            val prefs = activity.getSharedPreferences("auth", FragmentActivity.MODE_PRIVATE)
            val token = prefs.getString("token", null) ?: return
            val api = RetrofitClient.getCrudApiWithToken(token)

            activity.lifecycleScope.launch {
                try {
                    val response = api.updateItem(
                        item.id,
                        UpdateItemRequest(is_purchased = isPurchased)
                    )
                    if (response.isSuccessful) {
                        item.is_purchased = isPurchased
                        Toast.makeText(activity, "Zaktualizowano!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Błąd PUT: ${response.code()}", Toast.LENGTH_SHORT).show()
                        // Przywróć stan checkboxa
                        purchasedCheckBox.setOnCheckedChangeListener(null)
                        purchasedCheckBox.isChecked = !isPurchased
                        purchasedCheckBox.setOnCheckedChangeListener { _, checked ->
                            updateItemPurchased(item, checked)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ShoppingListAdapter", "Błąd", e)
                }
            }
        }

        private fun deleteItem(item: ShoppingItem) {
            val prefs = activity.getSharedPreferences("auth", FragmentActivity.MODE_PRIVATE)
            val token = prefs.getString("token", null) ?: return
            val api = RetrofitClient.getCrudApiWithToken(token)

            activity.lifecycleScope.launch {
                try {
                    val response = api.deleteItem(item.id)
                    if (response.isSuccessful) {
                        val position = items.indexOf(item)
                        if (position != -1) {
                            items.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        Toast.makeText(activity, "Usunięto przedmiot", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Błąd DELETE: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
