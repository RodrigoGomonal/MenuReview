package com.example.menureview.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menureview.viewmodel.ComentarioViewModel
import com.example.menureview.data.models.ComentarioEntity

@Composable
fun ComentariosScreen(
    restauranteId: Int,
    usuarioId: Int,
    viewModel: ComentarioViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Cargar comentarios al iniciar
    LaunchedEffect(restauranteId) {
        viewModel.loadComentariosByRestaurante(restauranteId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con promedio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Reseñas",
                    style = MaterialTheme.typography.headlineSmall
                )

                val promedio = viewModel.calcularPromedioCalificacion()
                if (promedio > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = String.format("%.1f", promedio),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = " (${state.comentarios.size})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Button(onClick = { showAddDialog = true }) {
                Text("Agregar reseña")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de comentarios
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "Error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            state.comentarios.isEmpty() -> {
                Text("No hay reseñas aún. ¡Sé el primero!")
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.comentarios) { comentario ->
                        ComentarioCard(comentario)
                    }
                }
            }
        }
    }

    // Dialog para agregar comentario
    if (showAddDialog) {
        AddComentarioDialog(
            onDismiss = { showAddDialog = false },
            onSubmit = { texto, calificacion ->
                viewModel.createComentario(
                    usuarioId = usuarioId,
                    restauranteId = restauranteId,
                    texto = texto,
                    calificacion = calificacion
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ComentarioCard(comentario: ComentarioEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Calificación
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(comentario.calificacion) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texto del comentario
            Text(
                text = comentario.comentario,
                style = MaterialTheme.typography.bodyMedium
            )

            // Fecha (si existe)
            comentario.fecha?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun AddComentarioDialogo(
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar reseña") },
        text = {
            Column {
                // Selector de estrellas
                Text("Calificación:")
                Row {
                    (1..5).forEach { star ->
                        IconButton(onClick = { calificacion = star }) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (star <= calificacion) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de texto
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    label = { Text("Escribe tu reseña") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(texto, calificacion) },
                enabled = texto.isNotBlank()
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}