package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.example.menureview.ui.components.MapaButton
import com.example.menureview.ui.components.NotificationButton
import com.example.menureview.ui.components.ProfileMenuButton
import com.example.menureview.viewmodel.UserViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import com.example.menureview.ui.components.RankingSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    viewModel: UserViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCEAF2)) // Fondo más moderno y suave
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        // ----- TOP BAR -----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                NotificationButton()
                Spacer(modifier = Modifier.width(8.dp))
                ProfileMenuButton(viewModel)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ----- BUSCADOR -----
        SearchBarSection()

        Spacer(modifier = Modifier.height(16.dp))

        // ----- RANKING -----
        RankingSection(
            modifier = Modifier
                .weight(4f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ----- CERCA DE TI -----
        NearYouSection(
            modifier = Modifier
                .weight(3f)
        )

        Spacer(modifier = Modifier.height(20.dp))
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
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Buscar",
                modifier = Modifier.size(24.dp)
            )
        },
        placeholder = { Text("Buscar restaurantes...", fontSize = 14.sp) },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFA68776),
            unfocusedBorderColor = Color(0xFFCCCCCC),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
    )
}


@Composable
fun NearYouSection(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Buscar Restaurantes Cercanos",
                fontSize = 20.sp,
                color = Color(0xFF533E25),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))
            MapaButton()
        }
    }
}
