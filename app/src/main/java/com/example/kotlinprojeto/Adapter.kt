import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinprojeto.R
import com.example.kotlinprojeto.ProjetoKotlin

class Adapter(
    private val shoppingLists: MutableList<ProjetoKotlin> // Use MutableList para permitir exclusão
) : RecyclerView.Adapter<Adapter.ShoppingListViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onDeleteClickListener: ((Int) -> Unit)? = null
    private var onViewListClickListener: ((Int) -> Unit)? = null

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val deleteButton: ImageView = itemView.findViewById(R.id.btn_delete)
        val viewButton: ImageView = itemView.findViewById(R.id.btn_viewList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]
        holder.titleTextView.text = shoppingList.title

        if (shoppingList.imageUri != null) {
            holder.imageView.setImageURI(shoppingList.imageUri)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_placeholder) // Imagem de placeholder
        }

        // Configurar o clique do item
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }

        // Configurar o clique do botão de exclusão
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener?.invoke(position)
        }

        // Configurar o clique do botão "olho" (viewButton) para ver a lista de itens
        holder.viewButton.setOnClickListener {
            onViewListClickListener?.invoke(position)
        }
    }

    override fun getItemCount() = shoppingLists.size

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnViewListClickListener(listener: (Int) -> Unit) {
        onViewListClickListener = listener // Novo listener para ver a lista
    }

    // Método para remover uma lista
    fun removeItem(position: Int) {
        shoppingLists.removeAt(position)
        notifyItemRemoved(position)
    }
}
