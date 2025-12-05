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
import com.example.menureview.data.models.RestauranteEntity
import com.example.menureview.viewmodel.RestauranteViewModel

@Composable
fun RestaurantesListScreen(
    viewModel: RestauranteViewModel,
    onRestauranteClick: (RestauranteEntity) -> Unit
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
                    items(state.restaurantes) { restaurante ->
                        RestauranteCardHorizontal(
                            restaurante = restaurante,
                            onClick = { onRestauranteClick(restaurante) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestauranteCardHorizontal(
    restaurante: RestauranteEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen del restaurante (izquierda)
            AsyncImage(
                model = restaurante.imagenurl ?: "https://img.freepik.com/vector-premium/vector-icono-imagen-predeterminado-pagina-imagen-faltante-diseno-sitio-web-o-aplicacion-movil-no-hay-foto-disponible_87543-11093.jpg",
                contentDescription = restaurante.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )

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
                        text = "📍 $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF656C73).copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }

                // Tags simulados (TODO: implementar tags reales)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TagChip("Japonés 🎌")
                    TagChip("Sushi 🍣")
                }

                // Calificación (TODO: calcular promedio real)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ 4.5",
                        fontSize = 14.sp,
                        color = Color(0xFFFFC107),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(25 reseñas)",
                        fontSize = 12.sp,
                        color = Color(0xFF656C73).copy(alpha = 0.6f)
                    )
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