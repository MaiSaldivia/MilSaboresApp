package com.example.milsaboresapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun observeAll(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    suspend fun getAll(): List<ProductoEntity>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ProductoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductoEntity>)

    @Update
    suspend fun update(product: ProductoEntity)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM productos WHERE LOWER(nombre) = LOWER(:name) LIMIT 1")
    suspend fun findByName(name: String): ProductoEntity?
}
