package com.example.menureview.data.repositories

import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.RestauranteApiService
import com.example.menureview.data.models.RestauranteEntity

/**
 * Repository para manejar operaciones de Restaurante
 * Encapsula la lógica de acceso a datos
 */
class RestauranteRepository {
    private val api: RestauranteApiService = ApiClient.create(RestauranteApiService::class.java)

    suspend fun getAllRestaurantes(): Result<List<RestauranteEntity>> {
        return try {
            val response = api.getAllRestaurantes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRestauranteById(id: Int): Result<RestauranteEntity> {
        return try {
            val response = api.getRestauranteById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Restaurante no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRestaurante(restaurante: RestauranteEntity): Result<RestauranteEntity> {
        return try {
            val response = api.createRestaurante(restaurante)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear restaurante"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRestaurante(id: Int, restaurante: RestauranteEntity): Result<RestauranteEntity> {
        return try {
            val response = api.updateRestaurante(id, restaurante)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar restaurante"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRestaurante(id: Int): Result<Unit> {
        return try {
            val response = api.deleteRestaurante(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar restaurante"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}