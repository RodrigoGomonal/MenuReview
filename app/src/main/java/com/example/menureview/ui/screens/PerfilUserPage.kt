package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.menureview.R

@Composable
fun PerfilUserPage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_circle_user),
            contentDescription = "Foto de perfil",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre: Juan Pérez")
        Text("Edad: 29")
        Text("Género: Masculino")
        Text("Correo: juanperez@gmail.com")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { /* Editar perfil */ }) {
            Text("Editar Perfil")
        }
    }
}
