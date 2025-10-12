package com.example.menureview.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentScreen: String,
    onNavigateTo: (route: String) -> Unit,
    showDrawer: Boolean,
    navController: NavController,
    // La función content AHORA espera recibir el PaddingValues del Scaffold
    content: @Composable (padding: PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route ?: "Main"
    val scaffoldContent: @Composable (PaddingValues) -> Unit = { innerPadding ->
        // 💡 CLAVE: Aplicamos el padding que viene del Scaffold al contenido (NavHost)
        content(innerPadding)
    }
    // 1. Lógica del Scaffold dentro del Drawer
    if (showDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(screenWidth * 2 / 3),
                    drawerContainerColor = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "MenuReview",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        HorizontalDivider()

                        Text(
                            text = "Secciones",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )

                        // --- Ítems de Navegación del Drawer ---

                        NavigationDrawerItem(
                            label = { Text("Inicio") },
                            selected = currentDestination == "Main",
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate("Main") {
                                        // Esto limpia la pila al navegar a Inicio
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                }
                            }
                        )

                        NavigationDrawerItem(
                            label = { Text("Perfil") },
                            selected = currentDestination == "PerfilUser",
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    onNavigateTo("PerfilUser")
                                }
                            }
                        )

                        NavigationDrawerItem(
                            label = { Text("Restaurante") },
                            selected = currentDestination == "PerfilRestaurant",
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    onNavigateTo("PerfilRestaurant")
                                }
                            }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        NavigationDrawerItem(
                            label = { Text("Configuración") },
                            selected = false,
                            onClick = { /* futuro */ }
                        )
                    }
                }
            },
            content = {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.height(56.dp),
                            title = {
                                Text(
                                    text = currentDestination.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, // Capitalizar el título
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    modifier = Modifier.size(36.dp),
                                    onClick = { scope.launch { drawerState.open() } }
                                ) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = "Abrir menú",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        )
                    },
                    content = scaffoldContent
                )
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // El contenido (NavHost) se dibuja dentro de este Box
            // y el padding de la TopBar es 0, ya que no hay TopBar.
            content(PaddingValues())
        }
    }
}