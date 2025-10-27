package com.huertohogar.huertohogarapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.huertohogar.huertohogarapp.screens.*
import com.huertohogar.huertohogarapp.viewmodel.CartViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController, cartViewModel) }
        composable("cart") { CartScreen(navController, cartViewModel) }
        composable("delivery") { DeliveryScreen(navController) }
        composable("profile") { ProfileScreen(navController = navController) }

    }
}


