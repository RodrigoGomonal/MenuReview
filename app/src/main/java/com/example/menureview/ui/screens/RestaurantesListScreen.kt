package com.example.menureview.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.menureview.viewmodel.Restaurante
import com.example.menureview.viewmodel.RestauranteViewModel

@Composable
fun RestaurantesListScreen(
    viewModel: RestauranteViewModel,
    onRestauranteClick: (Restaurante) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val bgSoft = Color(0xFFDCEAF2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgSoft)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Todos los Restaurantes",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF656C73),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadRestaurantes() }) {
                        Text("Reintentar")
                    }
                }
            }

            state.restaurantes.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay restaurantes disponibles")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.restaurantes) { restauranteConCalif ->
                        RestauranteCardHorizontal(
                            restauranteConCalif = restauranteConCalif,
                            onClick = { onRestauranteClick(restauranteConCalif) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestauranteCardHorizontal(
    restauranteConCalif: Restaurante,
    onClick: () -> Unit
) {
    val restaurante = restauranteConCalif.restaurante

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen del restaurante (izquierda)
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = restaurante.imagenurl ?: "https://placehold.co/140x140/4CAF50/FFF?text=Sin+Imagen",
                    contentDescription = restaurante.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                    onError = {
                        android.util.Log.e("ImageLoad", "Error cargando: ${restaurante.imagenurl}")
                    }
                )

                // Badge de calificación sobre la imagen
                if (restauranteConCalif.promedioCalificacion > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        color = Color(0xFFFFC107),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "⭐ ${String.format("%.1f", restauranteConCalif.promedioCalificacion)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Información del restaurante (derecha)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Nombre
                Text(
                    text = restaurante.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF656C73),
                    maxLines = 2
                )

                // Ubicación
                restaurante.ubicacion?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF656C73).copy(alpha = 0.7f),
                        maxLines = 1,
                        fontSize = 12.sp
                    )
                }

                // Tags desde la BD
                if (restauranteConCalif.tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        restauranteConCalif.tags.take(2).forEach { tag ->
                            TagChip(tag.nombre)
                        }
                        if (restauranteConCalif.tags.size > 2) {
                            TagChip("+${restauranteConCalif.tags.size - 2}")
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Calificación y reseñas
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (restauranteConCalif.promedioCalificacion > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "(${restauranteConCalif.totalComentarios} reseñas)",
                                fontSize = 11.sp,
                                color = Color(0xFF656C73).copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        Text(
                            text = "Sin reseñas",
                            fontSize = 12.sp,
                            color = Color(0xFF656C73).copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TagChip(text: String) {
    Surface(
        color = Color(0xFF4CAF50).copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = Color(0xFF533E25),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}