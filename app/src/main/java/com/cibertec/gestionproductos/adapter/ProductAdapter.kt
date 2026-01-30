package com.cibertec.gestionproductos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.gestionproductos.data.Product
import com.cibertec.gestionproductos.databinding.ItemProductoBinding

class ProductAdapter(
    private val listaProductos: MutableList<Product>,
    private val onDelete: ((Product) -> Unit)? = null,
    private val onEdit: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.binding.tvName.text = producto.nombre
        holder.binding.tvDescription.text = producto.descripcion
        holder.binding.tvIndicaciones.text = producto.indicaciones
        holder.binding.tvPrice.text = "S/ ${producto.precio}"

        holder.binding.btnDelete.setOnClickListener {
            onDelete?.invoke(producto)
        }

        holder.binding.btnEdit.setOnClickListener {
            onEdit?.invoke(producto)
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    fun removeProducto(product: Product) {
        val index = listaProductos.indexOf(product)
        if (index != -1) {
            listaProductos.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
