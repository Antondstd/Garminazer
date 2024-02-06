package net.garminazer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.garminazer.Destinations.HOME_ROUTE
import net.garminazer.home.HomeRoute


object Destinations {
    const val HOME_ROUTE = "home"
}

@Composable
fun GarminazerNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
    ) {
        composable(HOME_ROUTE) {
            HomeRoute(
//                onNavigateToSignIn = {
//                    navController.navigate("signin/$it")
//                },
            )
        }
    }
}