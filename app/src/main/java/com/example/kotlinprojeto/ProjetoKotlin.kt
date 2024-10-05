package com.example.kotlinprojeto

import Item
import android.net.Uri

data class ProjetoKotlin(
    val title: String,
    val imageUri: Uri? = null,
    val items: MutableList<Item> = mutableListOf(),
)
