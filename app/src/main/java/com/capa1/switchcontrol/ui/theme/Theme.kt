package com.capa1.switchcontrol.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Tus colores — accesibles desde toda la app
val BgPage      = Color(0xFF1C1C1E)
val BgCard      = Color(0xFF2C2C2E)
val AccentColor = Color(0xFF34C759)
val TextPrimary = Color(0xFFFFFFFF)

private val AppColorScheme = darkColorScheme(
    primary        = Color.White,
    secondary      = Color.LightGray,
    background     = BgPage,
    surface        = BgCard,
    onPrimary      = Color.Black,
    onBackground   = TextPrimary,
    onSurface      = TextPrimary,
)

@Composable
fun SwitchControlTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BgPage.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}