package com.example.menureview.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.menureview.R
import com.example.menureview.ui.theme.MenuReviewTheme
import com.example.menureview.viewmodel.ComentarioViewModel
import com.example.menureview.viewmodel.UserViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalificationPage(
    restauranteId: Int,
    comentarioViewModel: ComentarioViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    onBack: () -> Unit = {}
) {
    val state by comentarioViewModel.state.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val usuario = userState.usuarioActual

    var showAddDialog by remember { mutableStateOf(false) }

    val bgSoft = Color(0xFFDCEAF2)
    val starYellow = Color(0xFFFFD700)
    val bluePrimary = Color(0xFF4CAF50)
    val blueLight = Color(0xFF4CAF50).copy(alpha = 0.3f)
    val blueAvatar = Color(0xFF4CAF50)
    val buttonBlue = Color(0xFF4CAF50)

    // Cargar comentarios
    LaunchedEffect(restauranteId) {
        comentarioViewModel.loadComentariosByRestaurante(restauranteId)
    }

    // Mostrar toast de éxito
    LaunchedEffect(state.operacionExitosa) {
        if (state.operacionExitosa) {
            comentarioViewModel.resetState()
        }
    }

    val notaPromedio = state.promedioCalificacion
    val totalComentarios = state.comentarios.size
    val distribution = comentarioViewModel.getDistribution()
    val notasDistribucion = listOf(5, 4, 3, 2, 1)

    val gestureModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
            if (dragAmount > 30) onBack()
        }
    }
    MenuReviewTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background( color = MaterialTheme.colorScheme.secondary)
                .then(gestureModifier)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Botón volver
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh", true)

                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = "Volver",
                        tint = bluePrimary
                    )
                }
            }

            // Promedio de calificación
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (notaPromedio > 0) String.format("%.1f", notaPromedio) else "N/A",
                    color = bluePrimary,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    repeat(5) { i ->
                        val filled = i < notaPromedio.roundToInt()
                        Icon(
                            painter = painterResource(
                                if (filled) R.drawable.ic_star_full else R.drawable.ic_star_empty
                            ),
                            contentDescription = null,
                            tint = starYellow,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Text(
                    text = "$totalComentarios comentarios",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }

            // Barras de distribución
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val maxConteo = distribution.values.maxOrNull()?.toFloat() ?: 1f

                notasDistribucion.forEach { nota ->
                    val conteo = distribution[nota] ?: 0
                    val ancho = if (maxConteo > 0) conteo / maxConteo else 0f

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$nota",
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 14.sp,
                            modifier = Modifier.width(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(ancho.coerceAtLeast(0.05f))
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background( color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }

            // Loading o error
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error ?: "Error", color = Color.Red)
                }
            } else {
                // Lista de comentarios
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.comentarios) { comentario ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(14.dp),
                            tonalElevation = 3.dp,
                            shadowElevation = 3.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Avatar
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(blueAvatar),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "U",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            repeat(5) { i ->
                                                val filled = i < comentario.calificacion
                                                Icon(
                                                    painter = painterResource(
                                                        if (filled) R.drawable.ic_star_full
                                                        else R.drawable.ic_star_empty
                                                    ),
                                                    contentDescription = null,
                                                    tint = starYellow,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = comentario.calificacion.toString(),
                                                color = bluePrimary,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }

                                    comentario.fecha?.let { fecha ->
                                        Text(
                                            text = fecha.take(10), // Solo fecha YYYY-MM-DD
                                            color = bluePrimary.copy(alpha = 0.6f),
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = comentario.comentario,
                                    color = bluePrimary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Botón comentar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (usuario != null) {
                            showAddDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonBlue),
                    enabled = usuario != null
                ) {
                    Text(if (usuario != null) "Comentar" else "Inicia sesión para comentar",
                        color = Color.White)
                }
            }
        }
    }


    // Dialog para agregar comentario
    if (showAddDialog && usuario != null) {
        AddComentarioDialog(
            onDismiss = { showAddDialog = false },
            onSubmit = { texto, calificacion ->
                comentarioViewModel.createComentario(
                    usuarioId = usuario.id,
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
fun AddComentarioDialog(
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
                Text("Calificación:")
                Row {
                    (1..5).forEach { star ->
                        IconButton(onClick = { calificacion = star }) {
                            Icon(
                                painter = painterResource(
                                    if (star <= calificacion) R.drawable.ic_star_full
                                    else R.drawable.ic_star_empty
                                ),
                                contentDescription = null,
                                tint = Color(0xFFFFD700)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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