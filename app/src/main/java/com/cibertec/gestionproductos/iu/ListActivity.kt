package com.cibertec.gestionproductos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibertec.gestionproductos.adapter.ProductAdapter
import com.cibertec.gestionproductos.data.AppDatabase
import com.cibertec.gestionproductos.data.Product
import com.cibertec.gestionproductos.databinding.ActivityListBinding
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        binding.rvProductos.layoutManager = LinearLayoutManager(this)

        loadProductos()
    }

    private fun loadProductos() {
        lifecycleScope.launch {
            val productos = db.productDao().getAll().toMutableList()

            adapter = ProductAdapter(
                productos,
                onDelete = { producto ->
                    lifecycleScope.launch {
                        db.productDao().delete(producto)
                        adapter.removeProducto(producto) // ahora funciona porque adapter es propiedad
                        Toast.makeText(this@ListActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    }
                },
                onEdit = { producto ->
                    val intent = Intent(this@ListActivity, MainActivity::class.java)
                    intent.putExtra("productoEdit", producto)
                    startActivity(intent)
                }
            )

            binding.rvProductos.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadProductos() // Recarga lista al volver de editar
    }
}
