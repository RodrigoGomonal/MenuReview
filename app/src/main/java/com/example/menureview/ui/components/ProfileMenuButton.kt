package com.example.menureview.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.menureview.ui.screens.LoginDialog
import com.example.menureview.ui.screens.RegisterDialog
import com.example.menureview.viewmodel.UserViewModel
import com.example.menureview.R
import com.example.menureview.ui.theme.MenuReviewTheme

@Composable
fun ProfileMenuButton(userViewModel: UserViewModel) {
    val state by userViewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }

    MenuReviewTheme {
        Box {
            if (state.usuarioActual == null) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(40.dp))
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                ) {
                    DropdownMenuItem(
                        text = { Text("Iniciar sesión", color = MaterialTheme.colorScheme.background) },
                        onClick = {
                            expanded = false
                            showLoginDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Registrarse", color = MaterialTheme.colorScheme.background) },
                        onClick = {
                            expanded = false
                            showRegisterDialog = true
                        }
                    )
                }

                if (showLoginDialog) {
                    LoginDialog(
                        onDismiss = { showLoginDialog = false },
                        userViewModel = userViewModel,
                        onLoginSuccess = { showLoginDialog = false }
                    )
                }
                if (showRegisterDialog) {
                    RegisterDialog(
                        onDismiss = { showRegisterDialog = false },
                        userViewModel = userViewModel
                    )
                }
            } else {
                // Usuario autenticado
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFDCEAF2))
                        .clickable { expanded = !expanded }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen de usuario CAMBIAR A ICON BUTTON PARA AGREGAR EL LOGOUT DEBAJO
                    Image(
                        painter = painterResource(id = R.drawable.ic_circle_user),
                        contentDescription = "Usuario",
                        modifier = Modifier.size(32.dp),

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(state.usuarioActual?.name ?: "Usuario", color = Color(0xFF656C73))
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFFDCEAF2))
                ) {
                    DropdownMenuItem(
                        text = { Text("Ver perfil", color = Color(0xFF656C73)) },
                        onClick = {
                            expanded = false
                            // Aquí puedes navegar a PerfilUserPage
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión", color = Color.Red) },
                        onClick = {
                            expanded = false
                            userViewModel.logout()
                        }
                    )
                }
            }
        }
    }
}