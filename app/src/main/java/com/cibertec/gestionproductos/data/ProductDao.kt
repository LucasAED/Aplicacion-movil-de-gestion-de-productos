package com.cibertec.gestionproductos.data

import androidx.room.*

@Dao
interface ProductDao {

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM productos ORDER BY id DESC")
    suspend fun getAll(): List<Product>
}
