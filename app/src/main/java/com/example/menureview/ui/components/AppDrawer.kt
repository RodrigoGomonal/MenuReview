package com.example.menureview.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.menureview.R
import com.example.menureview.ui.theme.MenuReviewTheme
import kotlinx.coroutines.launch
import com.example.menureview.ui.components.AppLogo
import com.example.menureview.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    onNavigateTo: (route: String) -> Unit,
    showDrawer: Boolean,
    navController: NavController,
    viewModel: UserViewModel,
    content: @Composable (padding: PaddingValues) -> Unit
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route ?: "Main"
    val scaffoldContent: @Composable (PaddingValues) -> Unit = {
        innerPadding -> content(innerPadding)
    }

    MenuReviewTheme{
        val drawerItemsColors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.secondary,
            selectedTextColor = MaterialTheme.colorScheme.background,
            unselectedTextColor = MaterialTheme.colorScheme.secondary,
        )
        // 1. Lógica del Scaffold dentro del Drawer
        if (showDrawer) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.width(screenWidth * 2 / 3),
                        drawerContainerColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState())
                                .background(color = MaterialTheme.colorScheme.onSurface)
                        ) {
                            Spacer(Modifier.height(24.dp))

                            AppLogo(sizeFactor = 0.20f, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onPrimary)

                            Text(
                                text = "Menu",
                                modifier = Modifier
                                    .padding(16.dp),
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            // --- Ítems de Navegación del Drawer ---
                            NavigationDrawerItem(
                                label = { Text("Inicio") },
                                selected = currentDestination == "Main",
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate("Main") {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }

                                        }
                                    }
                                },
                                colors = drawerItemsColors
                            )
                            NavigationDrawerItem(
                                label = { Text("Perfil") },
                                selected = currentDestination == "PerfilUser",
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        onNavigateTo("PerfilUser")
                                    }
                                },
                                colors = drawerItemsColors
                            )
                            NavigationDrawerItem(
                                label = { Text("Restaurantes") },
                                selected = currentDestination == "RestaurantesList",
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        onNavigateTo("RestaurantesList")
                                    }
                                },
                                colors = drawerItemsColors
                            )
                            NavigationDrawerItem(
                                label = { Text("Mapa") },
                                selected = currentDestination == "FullMap",
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        onNavigateTo("FullMap")
                                    }
                                },
                                colors = drawerItemsColors
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onPrimary)

                            NavigationDrawerItem(
                                label = { Text("Configuración") },
                                selected = false,
                                onClick = { /* futuro */ },
                                colors = drawerItemsColors
                            )
                        }
                    }
                },
                content = {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                modifier = Modifier
                                    .height(70.dp)
                                    .background(color = MaterialTheme.colorScheme.secondary),
                                title = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        // ----- Ícono del menú hamburguesa -----
                                        IconButton(
                                            modifier = Modifier.size(40.dp),
                                            onClick = { scope.launch { drawerState.open() } }
                                        ) {
                                            Icon(
                                                Icons.Default.Menu,
                                                contentDescription = "Menú",
                                                modifier = Modifier.size(28.dp),
                                                tint = Color.White
                                            )
                                        }

                                        // ----- Logo centrado -----
                                        Image(
                                            painter = painterResource(id = R.drawable.img_menureview),
                                            contentDescription = "Logo",
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                        )

                                        // ----- Notificación + Perfil -----
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            NotificationButton()
                                            Spacer(Modifier.width(10.dp))
                                            ProfileMenuButton(viewModel)
                                        }
                                    }
                                },
                                navigationIcon = {},
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
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
                content(PaddingValues())
            }
        }
    }
}