package com.capa1.switchcontrol.ui.navigation

sealed class AppScreens(val route: String) {
    object SwListScreen: AppScreens("list")
    object ConfigScreen: AppScreens("config")
    object ModeConfigScreen: AppScreens("mode")
    object MaintenanceScreen: AppScreens("maintenance")
    object ColorPickerScreen: AppScreens("colorPicker")
    object TimerConfigScreen: AppScreens("timer")
}