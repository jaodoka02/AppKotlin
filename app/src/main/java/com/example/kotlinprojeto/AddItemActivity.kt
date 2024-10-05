package com.example.kotlinprojeto

import Item
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinprojeto.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var currentCategory: String = "Categoria Desconhecida" // Inicializando com uma categoria padrão

    companion object {
        val itemList = mutableListOf<Item>()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategorySpinner()
        setupSaveButton()
        setupCancelButton()
    }

    private fun setupCategorySpinner() {
        val categories = listOf("Alimentos", "Frutas/Verduras/Legumes", "Carnes", "Bebidas", "Frios", "Padaria", "Higiene", "Limpeza", "Outros")
        val icons = listOf(R.drawable.ic_alimentos, R.drawable.ic_frutas, R.drawable.ic_carnes, R.drawable.ic_bebidas, R.drawable.ic_frios, R.drawable.ic_padaria, R.drawable.ic_higiene, R.drawable.ic_limpeza, R.drawable.ic_default_category)

        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_item, categories) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = layoutInflater.inflate(R.layout.spinner_item, parent, false)
                val icon = view.findViewById<ImageView>(R.id.spinner_icon)
                val text = view.findViewById<TextView>(R.id.spinner_text)

                // Definindo o ícone corretamente
                if (position in icons.indices) {
                    icon.setImageResource(icons[position])
                }

                text.text = categories[position]
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return getView(position, convertView, parent)
            }
        }
        binding.categorySpinner.adapter = adapter

        // Defina o que acontece quando a categoria é selecionada
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currentCategory = categories[position] // Armazena a categoria selecionada

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Ação quando nada é selecionado
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveItem.setOnClickListener {
            val itemName = binding.editItemName.text.toString()
            val itemQuantity = binding.editItemQuantity.text.toString().toIntOrNull() ?: 1
            val itemUnit = binding.editItemUnit.text.toString()

            // Criar um novo item
            val newItem = Item(
                name = itemName,
                quantity = itemQuantity,
                unit = itemUnit,
                category = currentCategory,
                isPurchased = false
            )
            itemList.add(newItem) // Adicione o item na lista de compras

            val resultIntent = Intent()
            resultIntent.putExtra("NEW_ITEM", newItem)
            setResult(RESULT_OK, resultIntent)
            finish() // Fecha a Activity
        }
    }

    private fun setupCancelButton() {
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}
