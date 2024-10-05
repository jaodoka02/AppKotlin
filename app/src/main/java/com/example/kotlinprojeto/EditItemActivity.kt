package com.example.kotlinprojeto

import Item
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinprojeto.databinding.ActivityEditItemBinding

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private var itemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<Item>("ITEM") // Receber o item a ser editado
        itemPosition = intent.getIntExtra("POSITION", -1)

        // Preencher os campos com os dados do item
        binding.editItemName.setText(item?.name)
        binding.editItemQuantity.setText(item?.quantity.toString())
        binding.editItemUnit.setText(item?.unit)

        binding.btnSaveItem.setOnClickListener {
            // Verifica se a posição é válida antes de criar o item editado
            if (itemPosition >= 0) {
                val editedItem = item?.copy(
                    name = binding.editItemName.text.toString(),
                    quantity = binding.editItemQuantity.text.toString().toIntOrNull() ?: 1,
                    unit = binding.editItemUnit.text.toString()
                )

                // Passa o item editado e sua posição de volta
                val resultIntent = Intent().apply {
                    putExtra("EDITED_ITEM", editedItem)
                    putExtra("POSITION", itemPosition)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
