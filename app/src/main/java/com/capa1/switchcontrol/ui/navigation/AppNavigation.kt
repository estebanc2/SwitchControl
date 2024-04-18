package com.capa1.switchcontrol.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capa1.switchcontrol.ui.screens.ConfigScreen
import com.capa1.switchcontrol.ui.screens.SwListScreen


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "List"){
        composable(route = "List"){
            SwListScreen(navController)
        }
        composable(
            route = "Config/{id}",
            arguments = listOf(
                navArgument ("id"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            ConfigScreen(id = backStackEntry.arguments?.getString("id")?:"")
        }
    }
}
