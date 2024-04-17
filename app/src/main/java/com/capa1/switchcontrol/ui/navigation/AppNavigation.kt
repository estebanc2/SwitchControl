package com.capa1.switchcontrol.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capa1.switchcontrol.ui.screens.ColorPickerScreen
import com.capa1.switchcontrol.ui.screens.ConfigScreen
import com.capa1.switchcontrol.ui.screens.MaintenanceScreen
import com.capa1.switchcontrol.ui.screens.ModeConfigScreen
import com.capa1.switchcontrol.ui.screens.SwListScreen
import com.capa1.switchcontrol.ui.screens.TimerConfigScreen


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SwListScreen.route){

        composable(route = AppScreens.ConfigScreen.route){
            ConfigScreen(navController)
        }
        composable(route = AppScreens.MaintenanceScreen.route){
            MaintenanceScreen(navController)
        }
        composable(route = AppScreens.ModeConfigScreen.route){
            ModeConfigScreen(navController)
        }
        composable(route = AppScreens.SwListScreen.route){
            SwListScreen(navController)
        }
        composable(route = AppScreens.TimerConfigScreen.route){
            TimerConfigScreen(navController)
        }
    }
}
sealed class AppScreens(val route: String) {
    data object SwListScreen: AppScreens("list")
    data object ConfigScreen: AppScreens("config/id")
    data object ModeConfigScreen: AppScreens("mode")
    data object MaintenanceScreen: AppScreens("maintenance")
    data object ColorPickerScreen: AppScreens("colorPicker")
    data object TimerConfigScreen: AppScreens("timer")
}