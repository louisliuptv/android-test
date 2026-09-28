package com.example.emptyapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.emptyapp.feature.auth.presentation.LoginScreen
import com.example.emptyapp.feature.gps.presentation.detail.GpsDetailArgs
import com.example.emptyapp.feature.gps.presentation.detail.GpsDetailScreen
import com.example.emptyapp.feature.gps.presentation.list.GpsListScreen
import com.example.emptyapp.ui.LogoutViewModel

@Composable
fun EmptyAppNavHost(
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destinations.GPS_LIST) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Destinations.GPS_LIST) {
            val logoutViewModel: LogoutViewModel = hiltViewModel()
            GpsListScreen(
                onGpsClick = { id -> navController.navigate(Destinations.gpsDetail(id)) },
                onLogout = {
                    logoutViewModel.logout {
                        navController.navigate(Destinations.LOGIN) {
                            popUpTo(Destinations.GPS_LIST) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
            )
        }

        composable(
            route = Destinations.GPS_DETAIL,
            arguments = listOf(
                navArgument(GpsDetailArgs.GPS_ID) { type = NavType.IntType },
            ),
        ) {
            GpsDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
