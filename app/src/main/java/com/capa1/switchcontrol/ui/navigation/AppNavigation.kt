package com.capa1.switchcontrol.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capa1.switchcontrol.ui.ColorPickerScreen
import com.capa1.switchcontrol.ui.ConfigScreen
import com.capa1.switchcontrol.ui.SwListScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SwListScreen.route){
        composable(route = AppScreens.SwListScreen.route){
            SwListScreen(navController)
        }
        composable(route = AppScreens.ConfigScreen.route){
            ConfigScreen(navController)
        }
        composable(route = AppScreens.ColorPickerScreen.route){
            ColorPickerScreen(navController)
        }
    }
}