package com.example.menureview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.menureview.data.db.sampleRestaurants
import com.example.menureview.data.models.RestaurantEntity

@Composable
fun RankingSection(modifier: Modifier = Modifier) {

    // Ordenar la lista por score de mayor a menor
    val topRestaurants = remember {
        sampleRestaurants.sortedByDescending { it.score }
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

            // -------------------
            // 🔥 SECCIÓN DEL PODIO
            // -------------------
            PodiumSection(topRestaurants.take(3))

            Spacer(modifier = Modifier.height(18.dp))

            // -------------------
            // 🔥 LISTA NORMAL DESDE EL 4to
            // -------------------
            topRestaurants.drop(3).forEachIndexed { index, restaurant ->
                RankingItem(
                    position = index + 4,
                    name = restaurant.name,
                    score = restaurant.score ?: 0f
                )
            }
        }
    }
}

// -------------------------------------
// 🔥 COMPOSABLE DEL PODIO TOP 3
// -------------------------------------
@Composable
fun PodiumSection(top3: List<RestaurantEntity>) {

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
                restaurant = top3[1]
            )
        }

        // 1er lugar (centro)
        if (top3.isNotEmpty()) {
            PodiumItem(
                place = 1,
                color = gold,
                restaurant = top3[0],
                isFirst = true
            )
        }

        // 3er lugar (derecha)
        if (top3.size > 2) {
            PodiumItem(
                place = 3,
                color = bronze,
                restaurant = top3[2]
            )
        }
    }
}

// -------------------------------------
// 🔥 UN ELEMENTO DEL PODIO
// -------------------------------------
@Composable
fun PodiumItem(
    place: Int,
    color: Color,
    restaurant: RestaurantEntity,
    isFirst: Boolean = false
) {

    val height = if (isFirst) 130.dp else 100.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Estrella con la nota arriba
        Text(
            text = "⭐ ${restaurant.score}",
            fontSize = 18.sp,
            color = Color(0xFFFFC107)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Nombre del restaurante
        Text(
            text = restaurant.name,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Bloque del podio
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(height)
                .background(color, RoundedCornerShape(12.dp)),
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

// ---------------------------------------------------------
// 🔥 ITEM NORMAL PARA POSICIONES 4, 5, 6...
// ---------------------------------------------------------
@Composable
fun RankingItem(position: Int, name: String, score: Float) {
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
            text = "⭐ $score",
            fontSize = 16.sp,
            color = Color(0xFFFFC107)
        )
    }
}
