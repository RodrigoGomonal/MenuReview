package com.example.menureview.data.repositories

import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.ComentarioApiService
import com.example.menureview.data.models.ComentarioEntity
import com.example.menureview.data.models.ComentarioRequest
import com.example.menureview.data.models.ComentarioUpdateRequest

/**
 * Repository para manejar operaciones de Comentarios
 */
class ComentarioRepository {
    private val api: ComentarioApiService = ApiClient.create(ComentarioApiService::class.java)

    suspend fun getAllComentarios(): Result<List<ComentarioEntity>> {
        return try {
            val response = api.getAllComentarios()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComentariosByRestaurante(restauranteId: Int): Result<List<ComentarioEntity>> {
        return try {
            val response = api.getComentariosByRestaurante(restauranteId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar comentarios"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComentario(comentario: ComentarioRequest): Result<ComentarioEntity> {
        return try {
            val response = api.createComentario(comentario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Manejar error 400: usuario ya comentó
                val errorMsg = if (response.code() == 400) {
                    "Ya has comentado en este restaurante"
                } else {
                    "Error al crear comentario"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComentario(id: Int, comentario: ComentarioUpdateRequest): Result<ComentarioEntity> {
        return try {
            val response = api.updateComentario(id, comentario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar comentario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComentario(id: Int): Result<Unit> {
        return try {
            val response = api.deleteComentario(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar comentario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}