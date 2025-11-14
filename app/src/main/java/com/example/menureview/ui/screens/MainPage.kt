package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.ElevatedCard
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.menureview.R
import com.example.menureview.data.daos.UserDao
import com.example.menureview.ui.components.MapaButton
import com.example.menureview.ui.components.NotificationButton
import com.example.menureview.ui.components.ProfileMenuButton
import com.example.menureview.viewmodel.UserViewModel
import com.example.menureview.viewmodel.UserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavHostController,
    //userDao: UserDao,
//    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userDao))
    viewModel: UserViewModel
) {
    val state = viewModel.state

    var menuExpanded by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color(0xFFA68776))
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
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(45.dp)

            )
            NotificationButton()

            // Icono perfil derecha
            ProfileMenuButton(viewModel)
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
                painter = painterResource(id = R.drawable.ic_search),
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
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9B88F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Ranking de Restaurantes",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF656C73),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aquí irán los Top 10 restaurantes", color = Color.Black)
        }
    }
}
@Composable
fun NearYouSection(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCEAF2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Buscar Restaurantes cerca",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF656C73),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            //Text("Aquí irá Google Maps mostrando los restaurantes", color = Color.Black)

            MapaButton()
        }
    }
}