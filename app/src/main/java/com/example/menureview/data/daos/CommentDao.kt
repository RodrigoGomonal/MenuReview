package com.example.menureview.data.daos

import androidx.room.*
import com.example.menureview.data.models.CommentEntity

@Dao
interface CommentDao {
    // Buscar por restaurante
    @Query("SELECT * FROM comments WHERE restaurantId = :restaurantId ORDER BY date DESC")
    suspend fun getCommentsForRestaurant(restaurantId: Int): List<CommentEntity>
    // Buscar por usuario
    @Query("SELECT * FROM comments WHERE userId = :userId ORDER BY date DESC")
    suspend fun getCommentsByUser(userId: Int): List<CommentEntity>
    // Insertar
    @Insert
    suspend fun insert(comment: CommentEntity): Long
    // Actualizar
    @Update
    suspend fun update(comment: CommentEntity)
    // Eliminar
    @Delete
    suspend fun delete(comment: CommentEntity)
    // Eliminar por id
    @Query("DELETE FROM comments WHERE id = :id")
    suspend fun deleteById(id: Int)
}