package com.example.menureview.data.daos

import androidx.room.*
import com.example.menureview.data.models.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)//UPDATE, si el id existe lo actaliza
    suspend fun insertUser(user: UserEntity): Long
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>
    @Delete
    suspend fun deleteUser(user: UserEntity)
    //    @Update
//    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Int): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?


    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: Int)
    // Login query
    @Query("SELECT * FROM users WHERE email = :email AND pass = :pass LIMIT 1")
    suspend fun login(email: String, pass: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?
}
