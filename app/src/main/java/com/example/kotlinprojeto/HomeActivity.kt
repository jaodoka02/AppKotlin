package com.example.kotlinprojeto

import Adapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.appcompat.app.AlertDialog
import com.example.kotlinprojeto.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: Adapter
    private val projetoKotlins = mutableListOf<ProjetoKotlin>() // Lista de compras original
    private val filteredLists = mutableListOf<ProjetoKotlin>() // Lista de compras filtrada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializando os componentes
        val recyclerView = binding.recyclerViewLists
        binding.searchEditText // EditText para o campo de pesquisa
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 colunas

        // Inicializando o adapter com as listas filtradas
        adapter = Adapter(filteredLists)
        recyclerView.adapter = adapter

        // Encontrar os botões
        val fabAddList = binding.fabAddList
        val fabLogout = binding.fabLogout
        val fabSearch = binding.fabSearch

        fabLogout.setOnClickListener {
            Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // Implementação da funcionalidade de busca
        fabSearch.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim() // Obter o texto de pesquisa
            searchShoppingLists(query)
        }

        fabAddList.setOnClickListener {
            // Abrir AddEditListActivity para adicionar uma nova lista de compras
            val intent = Intent(this@HomeActivity, AddEditListActivity::class.java)
            startActivityForResult(intent, 1) // Request code 1
        }

        // Ao clicar em um item da lista, abrir para editar
        adapter.setOnItemClickListener { position ->
            val listToEdit = filteredLists[position]
            val intent = Intent(this@HomeActivity, AddEditListActivity::class.java).apply {
                putExtra("LIST_TITLE", listToEdit.title)
                putExtra("LIST_IMAGE_URI", listToEdit.imageUri.toString())
                putExtra("EDIT_MODE", true) // Indica que é modo de edição
                putExtra("LIST_POSITION", position) // Passa a posição da lista
            }
            startActivityForResult(intent, 1)
        }

        // Configurar o listener de exclusão
        adapter.setOnDeleteClickListener { position ->
            // Cria um AlertDialog para confirmação
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Você tem certeza que deseja excluir esta lista?")
                .setPositiveButton("Sim") { dialog, which ->
                    // Se o usuário confirmar, remove a lista
                    val listToRemove = filteredLists[position]
                    projetoKotlins.remove(listToRemove) // Remove da lista original
                    filteredLists.removeAt(position) // Remove da lista filtrada
                    adapter.notifyItemRemoved(position) // Notifica o adaptador da remoção
                    adapter.notifyItemRangeChanged(position, filteredLists.size) // Atualiza os itens restantes
                }
                .setNegativeButton("Não") { dialog, which ->
                    dialog.dismiss() // Apenas fecha o diálogo
                }
                .create()

            alertDialog.show()
        }

        adapter.setOnViewListClickListener { position ->
            // Pegue a lista de compras selecionada
            val selectedList = projetoKotlins[position]

            // Abra a Activity para ver os itens dessa lista
            val intent = Intent(this, ItemListActivity::class.java)
            intent.putExtra("SHOPPING_LIST_TITLE", selectedList.title)
            startActivity(intent)
        }
    }

    // Função de busca
    private fun searchShoppingLists(query: String) {
        filteredLists.clear() // Limpar lista filtrada

        if (query.isNotEmpty()) {
            // Filtrar listas com base no título ou nos itens dentro da lista
            for (list in projetoKotlins) {
                if (list.title.contains(query, ignoreCase = true)) {
                    filteredLists.add(list)
                }
            }
        } else {
            // Se o campo de busca estiver vazio, mostra todas as listas
            filteredLists.addAll(projetoKotlins)
        }

        // Atualiza o RecyclerView com os resultados filtrados
        adapter.notifyDataSetChanged()
    }

    // Receber a nova lista de compras adicionada ou atualizada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("LIST_TITLE")
            val imageUriString = data?.getStringExtra("LIST_IMAGE_URI")
            val imageUri: Uri? = imageUriString?.let { Uri.parse(it) }

            if (title != null) {
                // Verifica se estamos editando uma lista existente
                val isEditMode = data.getBooleanExtra("EDIT_MODE", false)
                if (isEditMode) {
                    val position = data.getIntExtra("LIST_POSITION", -1)
                    if (position != -1) {
                        // Atualiza a lista existente
                        projetoKotlins[position] = ProjetoKotlin(title, imageUri)
                        filteredLists[position] = projetoKotlins[position] // Atualiza na lista filtrada
                        // Reordena as listas após a edição
                        projetoKotlins.sortBy { it.title }
                        filteredLists.sortBy { it.title }
                        adapter.notifyItemChanged(position)
                    }
                } else {
                    // Adicionar nova lista de compras
                    val newList = ProjetoKotlin(title, imageUri)
                    projetoKotlins.add(newList)
                    // Reordena as listas após a adição
                    projetoKotlins.sortBy { it.title }
                    filteredLists.clear()
                    filteredLists.addAll(projetoKotlins) // Atualiza a lista filtrada
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
