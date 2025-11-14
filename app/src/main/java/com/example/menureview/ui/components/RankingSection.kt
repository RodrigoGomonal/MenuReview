package com.example.menureview.ui.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.menureview.data.db.sampleRestaurants

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

            // --- LISTA DE RESTAURANTES ---
            topRestaurants.forEachIndexed { index, restaurant ->
                RankingItem(
                    position = index + 1,
                    name = restaurant.name,
                    score = restaurant.score?:0f
                )
            }
        }
    }
}

@Composable
fun RankingItem(position: Int, name: String, score: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Número del ranking
        Text(
            text = "$position.",
            fontSize = 20.sp,
            color = Color(0xFF533E25),
            modifier = Modifier.width(32.dp)
        )

        // Nombre
        Text(
            text = name,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        // Score
        Text(
            text = "⭐ $score",
            fontSize = 16.sp,
            color = Color(0xFFFFC107)
        )
    }
}
