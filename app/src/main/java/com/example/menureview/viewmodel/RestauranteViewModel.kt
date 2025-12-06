package com.example.menureview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menureview.data.models.RestauranteEntity
import com.example.menureview.data.models.TagEntity
import com.example.menureview.data.repositories.ComentarioRepository
import com.example.menureview.data.repositories.RestauranteRepository
import com.example.menureview.data.repositories.RestauranteTagRepository
import com.example.menureview.data.repositories.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Data class para restaurante con calificación
data class Restaurante(
    val restaurante: RestauranteEntity,
    val promedioCalificacion: Float = 0f,
    val totalComentarios: Int = 0,
    val tags: List<TagEntity> = emptyList()
)
data class RestauranteState(
    val restaurantes: List<Restaurante> = emptyList(),
    val restauranteSeleccionado: Restaurante? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RestauranteViewModel(
    private val repository: RestauranteRepository = RestauranteRepository(),
    private val comentarioRepo: ComentarioRepository = ComentarioRepository(),
    private val tagRepo: TagRepository = TagRepository(),
    private val restauranteTagRepo: RestauranteTagRepository = RestauranteTagRepository()
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

                    // 2. Cargar todos los tags
                    val tagsResult = tagRepo.getAllTags()
                    val allTags = tagsResult.getOrNull() ?: emptyList()

                    // 3. Cargar relaciones restaurante-tag
                    val restauranteTagsResult = restauranteTagRepo.getAllRestauranteTags()
                    val restauranteTags = restauranteTagsResult.getOrNull() ?: emptyList()

                    // Cargar calificaciones para cada restaurante
                    val restaurantesConCalif = restaurantes.map { restaurante ->
                        val comentariosResult = comentarioRepo.getComentariosByRestaurante(restaurante.id)

                        val (promedio, total) = comentariosResult.fold(
                            onSuccess = { comentarios ->
                                val prom = if (comentarios.isNotEmpty()) {
                                    comentarios.map { it.calificacion }.average().toFloat()
                                } else 0f
                                Pair(prom, comentarios.size)
                            },
                            onFailure = { Pair(0f, 0) }
                        )

                        // Obtener tags del restaurante
                        val tagIds = restauranteTags
                            .filter { it.restaurante_id == restaurante.id }
                            .map { it.tag_id }

                        val tags = allTags.filter { it.id in tagIds }

                        Restaurante(
                            restaurante = restaurante,
                            promedioCalificacion = promedio,
                            totalComentarios = total,
                            tags = tags
                        )
                    }
                    // Ordenar por calificación
                    val ordenados = restaurantesConCalif.sortedByDescending { it.promedioCalificacion }

                    _state.value = RestauranteState(
                        restaurantes = ordenados,
                        isLoading = false
                    )
                    android.util.Log.d("RestauranteVM", "${restaurantes.size} restaurantes cargados")
                }
                .onFailure { error ->
                    _state.value = RestauranteState(
                        error = error.message ?: "Error al cargar restaurantes",
                        isLoading = false
                    )
                    android.util.Log.e("RestauranteVM", "Error: ${error.message}")
                }
        }
    }

    /**
     * Obtener top restaurantes por calificación
     */
    fun getTopRestaurantes(limit: Int = 5): List<Restaurante> {
        // TODO: Implementar cálculo real de calificación promedio
        return _state.value.restaurantes.take(limit)
    }

    /**
     * Seleccionar restaurante
     */
    fun selectRestaurante(restaurante: Restaurante) {
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