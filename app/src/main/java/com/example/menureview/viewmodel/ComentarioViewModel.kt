package com.example.menureview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menureview.data.models.ComentarioEntity
import com.example.menureview.data.models.ComentarioRequest
import com.example.menureview.data.repositories.ComentarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ComentarioState(
    val comentarios: List<ComentarioEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val operacionExitosa: Boolean = false,
    val promedioCalificacion: Float = 0f
)

class ComentarioViewModel(
    private val repository: ComentarioRepository = ComentarioRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ComentarioState())
    val state: StateFlow<ComentarioState> = _state

    /**
     * Cargar comentarios de un restaurante
     */
    fun loadComentariosByRestaurante(restauranteId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            repository.getComentariosByRestaurante(restauranteId)
                .onSuccess { comentarios ->
                    val promedio = if (comentarios.isNotEmpty()) {
                        comentarios.map { it.calificacion }.average().toFloat()
                    } else {
                        0f
                    }

                    _state.value = ComentarioState(
                        comentarios = comentarios.sortedByDescending { it.fecha },
                        isLoading = false,
                        promedioCalificacion = promedio
                    )
                    android.util.Log.d("ComentarioVM", "✅ ${comentarios.size} comentarios, promedio: $promedio")
                }
                .onFailure { error ->
                    _state.value = ComentarioState(
                        error = error.message ?: "Error al cargar comentarios",
                        isLoading = false
                    )
                }
        }
    }

    /**
     * Crear nuevo comentario
     */
    fun createComentario(
        usuarioId: Int,
        restauranteId: Int,
        texto: String,
        calificacion: Int
    ) {
        if (texto.isBlank()) {
            _state.value = _state.value.copy(error = "El comentario no puede estar vacío")
            return
        }

        if (calificacion !in 1..5) {
            _state.value = _state.value.copy(error = "La calificación debe ser entre 1 y 5")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val request = ComentarioRequest(
                usuario_id = usuarioId,
                restaurante_id = restauranteId,
                comentario = texto,
                calificacion = calificacion
            )

            repository.createComentario(request)
                .onSuccess {
                    // Recargar comentarios
                    loadComentariosByRestaurante(restauranteId)
                    _state.value = _state.value.copy(operacionExitosa = true)
                    android.util.Log.d("ComentarioVM", "✅ Comentario creado")
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Error al crear comentario",
                        isLoading = false
                    )
                }
        }
    }

    /**
     * Calcular distribución de calificaciones
     */
    fun getDistribution(): Map<Int, Int> {
        val comentarios = _state.value.comentarios
        val distribution = mutableMapOf<Int, Int>().apply {
            (1..5).forEach { put(it, 0) }
        }

        comentarios.forEach { comentario ->
            val nota = comentario.calificacion.coerceIn(1, 5)
            distribution[nota] = distribution.getValue(nota) + 1
        }

        return distribution
    }

    /**
     * Calcular promedio de calificaciones
     */
    fun calcularPromedioCalificacion(): Float {
        val comentarios = _state.value.comentarios
        if (comentarios.isEmpty()) return 0f
        return comentarios.map { it.calificacion }.average().toFloat()
    }

    /**
     * Resetear estado
     */
    fun resetState() {
        _state.value = _state.value.copy(
            error = null,
            operacionExitosa = false
        )
    }
}