package com.example.menureview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.menureview.data.models.RestauranteEntity
import com.example.menureview.viewmodel.RestauranteViewModel

@Composable
fun RankingSection(
    viewModel: RestauranteViewModel,
    modifier: Modifier = Modifier,
    onRestauranteClick: (RestauranteEntity) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    // Ordenar restaurantes (por ahora por ID, luego por calificación real)
    val topRestaurants = remember(state.restaurantes) {
        state.restaurantes.take(5) // Top 5
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ranking de Restaurantes",
                fontSize = 22.sp,
                color = Color(0xFF533E25),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                state.error != null -> {
                    Text(
                        text = "Error al cargar ranking",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                topRestaurants.isEmpty() -> {
                    Text(
                        text = "No hay restaurantes disponibles",
                        color = Color(0xFF533E25),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    // PODIO TOP 3
                    PodiumSection(topRestaurants.take(3), onRestauranteClick)

                    Spacer(modifier = Modifier.height(18.dp))

                    // LISTA NORMAL DESDE EL 4to
                    topRestaurants.drop(3).forEachIndexed { index, restaurant ->
                        RankingItem(
                            position = index + 4,
                            name = restaurant.nombre,
                            score = 4.5f, // TODO: Calcular promedio real de comentarios
                            onClick = { onRestauranteClick(restaurant) }
                        )
                    }
                }
            }
        }
    }
}

// COMPOSABLE DEL PODIO TOP 3
@Composable
fun PodiumSection(
    top3: List<RestauranteEntity>,
    onRestauranteClick: (RestauranteEntity) -> Unit
) {
    val gold = Color(0xFFFFD700)
    val silver = Color(0xFFC0C0C0)
    val bronze = Color(0xFFCD7F32)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // 2do lugar (izquierda)
        if (top3.size > 1) {
            PodiumItem(
                place = 2,
                color = silver,
                restaurant = top3[1],
                onClick = { onRestauranteClick(top3[1]) }
            )
        }

        // 1er lugar (centro)
        if (top3.isNotEmpty()) {
            PodiumItem(
                place = 1,
                color = gold,
                restaurant = top3[0],
                isFirst = true,
                onClick = { onRestauranteClick(top3[0]) }
            )
        }

        // 3er lugar (derecha)
        if (top3.size > 2) {
            PodiumItem(
                place = 3,
                color = bronze,
                restaurant = top3[2],
                onClick = { onRestauranteClick(top3[2]) }
            )
        }
    }
}

// UN ELEMENTO DEL PODIO
@Composable
fun PodiumItem(
    place: Int,
    color: Color,
    restaurant: RestauranteEntity,
    isFirst: Boolean = false,
    onClick: () -> Unit = {}
) {
    val height = if (isFirst) 130.dp else 100.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        // Estrella con la nota arriba
        Text(
            text = "⭐ 4.5", // TODO: Calcular promedio real
            fontSize = 16.sp,
            color = Color(0xFFFFC107)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Nombre del restaurante
        Text(
            text = restaurant.nombre,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Bloque del podio (clickeable)
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(height)
                .background(color, RoundedCornerShape(12.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = place.toString(),
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ITEM NORMAL PARA POSICIONES 4+
@Composable
fun RankingItem(
    position: Int,
    name: String,
    score: Float,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$position.",
            fontSize = 20.sp,
            color = Color(0xFF533E25),
            modifier = Modifier.width(32.dp)
        )
        Text(
            text = name,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "⭐ ${String.format("%.1f", score)}",
            fontSize = 16.sp,
            color = Color(0xFFFFC107)
        )
    }
}