package com.example.menureview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menureview.data.models.RestauranteEntity
import com.example.menureview.data.repositories.RestauranteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RestauranteState(
    val restaurantes: List<RestauranteEntity> = emptyList(),
    val restauranteSeleccionado: RestauranteEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RestauranteViewModel(
    private val repository: RestauranteRepository = RestauranteRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RestauranteState())
    val state: StateFlow<RestauranteState> = _state

    init {
        // Cargar restaurantes al iniciar
        loadRestaurantes()
    }

    /**
     * Cargar todos los restaurantes
     */
    fun loadRestaurantes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            repository.getAllRestaurantes()
                .onSuccess { restaurantes ->
                    _state.value = RestauranteState(
                        restaurantes = restaurantes,
                        isLoading = false
                    )
                    android.util.Log.d("RestauranteVM", "✅ ${restaurantes.size} restaurantes cargados")
                }
                .onFailure { error ->
                    _state.value = RestauranteState(
                        error = error.message ?: "Error al cargar restaurantes",
                        isLoading = false
                    )
                    android.util.Log.e("RestauranteVM", "❌ Error: ${error.message}")
                }
        }
    }

    /**
     * Cargar un restaurante específico por ID
     */
    fun loadRestauranteById(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            repository.getRestauranteById(id)
                .onSuccess { restaurante ->
                    _state.value = _state.value.copy(
                        restauranteSeleccionado = restaurante,
                        isLoading = false
                    )
                    android.util.Log.d("RestauranteVM", "✅ Restaurante cargado: ${restaurante.nombre}")
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Restaurante no encontrado",
                        isLoading = false
                    )
                }
        }
    }

    /**
     * Obtener top restaurantes por calificación
     */
    fun getTopRestaurantes(limit: Int = 5): List<RestauranteEntity> {
        // TODO: Implementar cálculo real de calificación promedio
        return _state.value.restaurantes.take(limit)
    }

    /**
     * Seleccionar restaurante
     */
    fun selectRestaurante(restaurante: RestauranteEntity) {
        _state.value = _state.value.copy(restauranteSeleccionado = restaurante)
    }

    /**
     * Limpiar restaurante seleccionado
     */
    fun clearSelection() {
        _state.value = _state.value.copy(restauranteSeleccionado = null)
    }

    /**
     * Resetear errores
     */
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}