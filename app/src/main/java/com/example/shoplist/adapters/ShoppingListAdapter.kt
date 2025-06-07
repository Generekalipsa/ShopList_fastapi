package com.example.shoplist

import ShoppingItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ShoppingListAdapter(
    private val items: MutableList<ShoppingItem>,
    private val onItemClicked: (ShoppingItem) -> Unit

) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.textItemName)
        val qtyView: TextView = view.findViewById(R.id.textItemQty)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

        init {
            view.setOnClickListener {
                onItemClicked(items[adapterPosition])
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    item.is_purchased = isChecked
                    ShoppingListStorage.save(itemView.context, items)
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val removedItem = items[position]
                    items.removeAt(position)
                    ShoppingListData.removeItem(removedItem, view.context)
                    notifyItemRemoved(position)

                    Snackbar.make(
                        itemView,
                        "Usunięto: ${removedItem.name}",
                        Snackbar.LENGTH_SHORT
                    ).show()
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
        val item = items[position]
        holder.nameView.text = item.name
        holder.qtyView.text = "Ilość: ${item.quantity}"
        holder.checkBox.isChecked = item.is_purchased
    }

    override fun getItemCount() = items.size
}