package com.example.kotlinprojeto

import Item
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojeto.databinding.ActivityItemListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemListBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var projetoKotlin: ProjetoKotlin
    private val REQUEST_EDIT_ITEM = 1

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listTitle = intent.getStringExtra("SHOPPING_LIST_TITLE") ?: "Nova Lista"
        projetoKotlin = ProjetoKotlin(title = listTitle)

        loadSavedItems()

        itemAdapter = ItemAdapter(groupItems(projetoKotlin.items))

        binding.recyclerViewItems.adapter = itemAdapter
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)

        setupButtonListeners()

        binding.tvListName.text = projetoKotlin.title
    }

    private fun loadSavedItems() {
        val sharedPreferences = getSharedPreferences("ShoppingListApp", MODE_PRIVATE)
        val savedListJson = sharedPreferences.getString("SAVED_LIST_${projetoKotlin.title}", null)
        if (savedListJson != null) {
            val gson = Gson()
            val type = object : TypeToken<ProjetoKotlin>() {}.type
            projetoKotlin = gson.fromJson(savedListJson, type)
        }
    }

    private fun setupButtonListeners() {
        binding.btnAddItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, 1)
        }

        binding.btnEditItem.setOnClickListener {
            val selectedItem = itemAdapter.getSelectedItem()
            val position = itemAdapter.getSelectedItemPosition()

            if (selectedItem != null && position >= 0) {
                val intent = Intent(this, EditItemActivity::class.java).apply {
                    putExtra("ITEM", selectedItem)
                    putExtra("POSITION", position)
                }
                startActivityForResult(intent, REQUEST_EDIT_ITEM)
            }
        }

        binding.btnDeleteItem.setOnClickListener {
            val position = itemAdapter.getSelectedItemPosition()
            if (position >= 0) {
                val selectedItem = itemAdapter.getSelectedItem()
                if (selectedItem != null) {
                    projetoKotlin.items.remove(selectedItem)
                    itemAdapter.updateItems(groupItems(projetoKotlin.items))
                    itemAdapter.notifyItemRemoved(position)
                    itemAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newItem = data?.getParcelableExtra<Item>("NEW_ITEM")
            if (newItem != null) {
                projetoKotlin.items.add(newItem)
                projetoKotlin.items.sortBy { it.name }
                itemAdapter.updateItems(groupItems(projetoKotlin.items))
                itemAdapter.notifyItemInserted(projetoKotlin.items.size - 1)

                Log.d("ProjetoKotlin", "Item added: ${newItem.name}, Total items: ${projetoKotlin.items.size}")
            }
        }

        if (requestCode == REQUEST_EDIT_ITEM && resultCode == Activity.RESULT_OK) {
            val editedItem = data?.getParcelableExtra<Item>("EDITED_ITEM")
            val position = data?.getIntExtra("POSITION", -1) ?: -1

            if (position >= 0 && editedItem != null) {
                projetoKotlin.items[position] = editedItem
                itemAdapter.updateItems(groupItems(projetoKotlin.items))
                itemAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveItems()
    }

    private fun saveItems() {
        val sharedPreferences = getSharedPreferences("ShoppingListApp", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val jsonList = gson.toJson(projetoKotlin)

        editor.putString("SAVED_LIST_${projetoKotlin.title}", jsonList)
        editor.apply()
    }

    private fun groupItems(items: List<Item>): Map<String, List<Item>> {
        return items.groupBy { it.category }
    }
}
