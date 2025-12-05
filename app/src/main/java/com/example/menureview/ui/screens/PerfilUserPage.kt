package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.menureview.R
import com.example.menureview.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun PerfilUserPage(userViewModel: UserViewModel) {
    val state by userViewModel.state.collectAsState()

    if (state.usuarioActual == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No has iniciado sesión", color = Color.Gray)
        }
    } else {
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
            Text("Nombre: ${state.usuarioActual?.nombre}")
            Text("Correo: ${state.usuarioActual?.correo}")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { /* Editar perfil */ }) {
                Text("Editar Perfil")
            }
        }
    }
}