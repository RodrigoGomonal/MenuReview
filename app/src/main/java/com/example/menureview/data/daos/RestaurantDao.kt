package com.example.menureview.data.daos

import androidx.room.*
import com.example.menureview.data.models.RestaurantEntity

@Dao
interface RestaurantDao {
    // Listar
    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    suspend fun getAll(): List<RestaurantEntity>
    // Buscar por restaurante
    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getById(id: Int): RestaurantEntity?
    // Buscar por tag
    @Query("SELECT * FROM restaurants WHERE tagId = :tagId")
    suspend fun getByTag(tagId: Int): List<RestaurantEntity>
    // Buscar por nombre
    @Query("SELECT * FROM restaurants WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<RestaurantEntity>
    // Insertar
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: RestaurantEntity)
    // Insertar varios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(restaurants: List<RestaurantEntity>)
    // Actualizar
    @Update
    suspend fun update(restaurant: RestaurantEntity)
    // Eliminar
    @Delete
    suspend fun delete(restaurant: RestaurantEntity)
}