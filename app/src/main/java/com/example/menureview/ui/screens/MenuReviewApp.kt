package com.example.menureview


import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.menureview.ui.components.AppDrawer
import com.example.menureview.ui.screens.*
import com.example.menureview.viewmodel.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.menureview.ui.components.FullMapScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MenuReviewApp(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ViewModels
    val locationViewModel: LocationViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(
            context.applicationContext as Application
        )
    )

    remember { AuthRepository(context) }
    val restauranteViewModel: RestauranteViewModel = viewModel()
    val comentarioViewModel: ComentarioViewModel = viewModel()

    // Páginas donde el Drawer debe estar desactivado
    val noDrawerScreens = listOf("Calification", "FullMap")
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route ?: "Main"
    val showDrawer = !noDrawerScreens.any { currentRoute.startsWith(it) }

    AppDrawer(
        onNavigateTo = { route -> navController.navigate(route) },
        showDrawer = showDrawer,
        navController = navController,
        viewModel = userViewModel,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Main",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Página principal
            composable("Main") {
                MainPage(
                    restauranteViewModel = restauranteViewModel,
                    locationViewModel = locationViewModel
                )
            }

            // Perfil de usuario
            composable("PerfilUser") {
                PerfilUserPage(userViewModel)
            }

            // Lista de restaurantes
            composable("RestaurantesList") {
                RestaurantesListScreen(
                    viewModel = restauranteViewModel,
                    navController = navController,
                    onRestauranteClick = { restauranteConCalif ->
                        restauranteViewModel.selectRestaurante(restauranteConCalif)
                        navController.navigate("PerfilRestaurant")
                    }
                )
            }

            // Perfil de restaurante
            composable("PerfilRestaurant") {
                PerfilRestaurantePage(
                    navController = navController,
                    restauranteViewModel = restauranteViewModel,
                    comentarioViewModel = comentarioViewModel
                )
            }

            // Calificaciones (con ID de restaurante)
            composable(
                route = "Calification/{restauranteId}",
                arguments = listOf(
                    navArgument("restauranteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val restauranteId = backStackEntry.arguments?.getInt("restauranteId") ?: 0
                CalificationPage(
                    restauranteId = restauranteId,
                    comentarioViewModel = comentarioViewModel,
                    userViewModel = userViewModel,
                    navController = navController,
                    onBack = { navController.popBackStack() }
                )
            }

            // Mapa completo
            composable("FullMap") {
                FullMapScreen(
                    lastKnownLocation = locationViewModel.lastKnownLocation,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}