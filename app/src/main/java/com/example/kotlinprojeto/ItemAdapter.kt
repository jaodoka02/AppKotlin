package com.example.kotlinprojeto

import Item
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private var groupedItems: Map<String, List<Item>>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var selectedItemPosition: Int = -1

    private val categoryIcons = mapOf(
        "Alimentos" to R.drawable.ic_alimentos,
        "Frutas/Verduras/Legumes" to R.drawable.ic_frutas,
        "Carnes" to R.drawable.ic_carnes,
        "Bebidas" to R.drawable.ic_bebidas,
        "Frios" to R.drawable.ic_frios,
        "Padaria" to R.drawable.ic_padaria,
        "Higiene" to R.drawable.ic_higiene,
        "Limpeza" to R.drawable.ic_limpeza,
        "Outros" to R.drawable.ic_default_category
    )

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemPurchasedCheckBox: CheckBox = itemView.findViewById(R.id.item_purchased_checkbox)
        val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity)
        val categoryIcon: ImageView = itemView.findViewById(R.id.item_category_icon)
        val itemUnit: TextView = itemView.findViewById(R.id.item_unit)
        val itemName: TextView = itemView.findViewById(R.id.item_name)

        init {
            itemPurchasedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val item = getItem(adapterPosition)
                item?.let {
                    it.isPurchased = isChecked
                    updateItems(groupedItems)
                }
            }

            itemView.setOnClickListener {
                if (selectedItemPosition != adapterPosition) {
                    selectedItemPosition = adapterPosition
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (nonPurchasedItems, purchasedItems) = getItemsSeparated()
        val itemList = if (position < nonPurchasedItems.size) {
            nonPurchasedItems
        } else {
            purchasedItems
        }

        val currentItem = if (position < nonPurchasedItems.size) {
            nonPurchasedItems[position]
        } else {
            purchasedItems[position - nonPurchasedItems.size]
        }

        holder.itemName.text = currentItem.name
        holder.itemQuantity.text = currentItem.quantity.toString()
        holder.itemUnit.text = currentItem.unit
        holder.itemPurchasedCheckBox.isChecked = currentItem.isPurchased

        holder.categoryIcon.setImageResource(categoryIcons[currentItem.category] ?: R.drawable.ic_default_category)

        holder.itemView.setBackgroundColor(if (selectedItemPosition == position) Color.LTGRAY else Color.WHITE)
    }

    private fun getItemsSeparated(): Pair<List<Item>, List<Item>> {
        val purchasedItems = mutableListOf<Item>()
        val nonPurchasedItems = mutableListOf<Item>()

        for (items in groupedItems.values) {
            for (item in items) {
                if (item.isPurchased) {
                    purchasedItems.add(item)
                } else {
                    nonPurchasedItems.add(item)
                }
            }
        }
        return Pair(nonPurchasedItems, purchasedItems)
    }

    private fun getItem(position: Int): Item? {
        val (nonPurchasedItems, purchasedItems) = getItemsSeparated()
        return when {
            position < nonPurchasedItems.size -> nonPurchasedItems[position]
            position < nonPurchasedItems.size + purchasedItems.size -> purchasedItems[position - nonPurchasedItems.size]
            else -> null
        }
    }

    fun getSelectedItem(): Item? {
        return getItem(selectedItemPosition)
    }

    fun getSelectedItemPosition(): Int {
        return selectedItemPosition
    }

    fun updateItems(newItems: Map<String, List<Item>>) {
        groupedItems = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        val (nonPurchasedItems, purchasedItems) = getItemsSeparated()
        return nonPurchasedItems.size + purchasedItems.size
    }
}
