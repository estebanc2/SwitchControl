package com.capa1.switchcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.capa1.switchcontrol.ui.MainScreen
import com.capa1.switchcontrol.ui.theme.SwitchControlTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwitchControlTheme {
                MainScreen()
            }
        }
    }
}
