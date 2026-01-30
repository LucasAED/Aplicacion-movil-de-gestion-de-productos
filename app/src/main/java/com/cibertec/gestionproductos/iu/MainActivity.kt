package com.cibertec.gestionproductos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cibertec.gestionproductos.data.AppDatabase
import com.cibertec.gestionproductos.data.Product
import com.cibertec.gestionproductos.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    private var productoEdit: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        productoEdit = intent.getParcelableExtra("productoEdit")
        if (productoEdit != null) {
            binding.etNombre.setText(productoEdit!!.nombre)
            binding.etDescripcion.setText(productoEdit!!.descripcion)
            binding.etIndicaciones.setText(productoEdit!!.indicaciones)
            binding.etPrecio.setText(productoEdit!!.precio.toString())
            binding.btnGuardar.text = "Actualizar Producto"
        }

        binding.btnGuardar.setOnClickListener {
            if (productoEdit != null) {
                actualizarProducto(productoEdit!!)
            } else {
                guardarProducto()
            }
        }

        binding.btnVerLista.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }
    }

    private fun guardarProducto() {
        val nombre = binding.etNombre.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val indicaciones = binding.etIndicaciones.text.toString()
        val precioStr = binding.etPrecio.text.toString()

        if (nombre.isBlank() || descripcion.isBlank() || indicaciones.isBlank() || precioStr.isBlank()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Product(nombre = nombre, descripcion = descripcion, indicaciones = indicaciones, precio = precio)

        lifecycleScope.launch {
            db.productDao().insert(producto)
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Producto guardado", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
        }
    }

    private fun actualizarProducto(producto: Product) {
        val nombre = binding.etNombre.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val indicaciones = binding.etIndicaciones.text.toString()
        val precioStr = binding.etPrecio.text.toString()

        if (nombre.isBlank() || descripcion.isBlank() || indicaciones.isBlank() || precioStr.isBlank()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val productoActualizado = producto.copy(
            nombre = nombre,
            descripcion = descripcion,
            indicaciones = indicaciones,
            precio = precio
        )

        lifecycleScope.launch {
            db.productDao().update(productoActualizado)
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Producto actualizado", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                finish() // Regresa a la lista
            }
        }
    }

    private fun limpiarCampos() {
        binding.etNombre.text?.clear()
        binding.etDescripcion.text?.clear()
        binding.etIndicaciones.text?.clear()
        binding.etPrecio.text?.clear()
        binding.btnGuardar.text = "Guardar Producto"
        productoEdit = null
    }
}
