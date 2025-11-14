package com.example.menureview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.menureview.ui.screens.*
import com.example.menureview.viewmodel.UserViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    //userDao: UserDao
    viewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "Main"
    ) {
        composable("Main") { MainPage(navController, viewModel) }
        composable("PerfilUser") { PerfilUserPage(navController , viewModel) }
        composable("PerfilRestaurant") { PerfilRestaurantePage(navController) }
        composable("Calification") { CalificationPage(navController) }
    }

}
