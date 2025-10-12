package com.example.menureview.data.daos

import androidx.room.*
import com.example.menureview.data.models.TagEntity

@Dao
interface TagDao {
    @Query("SELECT * FROM tags ORDER BY name ASC")
    suspend fun getAll(): List<TagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity): Long

    @Update
    suspend fun update(tag: TagEntity)

    @Delete
    suspend fun delete(tag: TagEntity)

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getById(id: Int): TagEntity?

    @Query("SELECT * FROM tags WHERE name LIKE '%' || :name || '%'")
    suspend fun searchByName(name: String): List<TagEntity>
}