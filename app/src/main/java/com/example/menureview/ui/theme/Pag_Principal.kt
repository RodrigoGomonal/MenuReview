package com.example.menureview.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.menureview.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCEE8FC))
            .statusBarsPadding()
            .padding(bottom = 24.dp, start = 8.dp, end = 8.dp)

    ) {

        // ----- 1F -----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo izquierda
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )
            // Icono perfil derecha
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Perfil",
                modifier = Modifier.size(40.dp)
            )
        }
        //Espacio
        Spacer(modifier = Modifier.height(4.dp))
        // ----- 2F -----
        SearchBarSection()
        //Espacio
        Spacer(modifier = Modifier.height(6.dp))
        // ----- 3F -----
        RankingSection(modifier = Modifier.weight(4f))
        //Espacio
        Spacer(modifier = Modifier.height(10.dp))
        // ----- 4F -----
        NearYouSection(modifier = Modifier.weight(3f))
        //Espacio
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SearchBarSection() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Buscar",
                modifier = Modifier.size(24.dp)
            )
        },
        placeholder = { Text("Buscar restaurantes...") },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
fun RankingSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF59D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Ranking de Restaurantes",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aquí irán los Top 10 restaurantes", color = Color.Black)
        }
    }
}

@Composable
fun NearYouSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFA5D6A7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Restaurantes cerca de ti",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aquí irá Google Maps mostrando los restaurantes", color = Color.Black)
        }
    }
}