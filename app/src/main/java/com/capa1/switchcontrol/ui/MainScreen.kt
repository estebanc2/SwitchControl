package com.capa1.switchcontrol.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(
    viewModel: ViewModel = hiltViewModel()
) {
    val screenModifiers by viewModel.screenModifiers.collectAsState()
    Box(Modifier.fillMaxSize()) {
        if (!screenModifiers.showList) {
            ShowSwitches(
                list = viewModel.getCalList(),
                onExit = { viewModel.showList(false) }
            )
        }
    }
}

@Composable
fun ShowSwitches(
    list: List<String>,
    onExit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        item {
            Text(
                text = "Parametros de calibración en OnMast",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(list) { calValue ->
            Text(
                text = calValue,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        item {
            TextButton(
                onClick = { onExit() },
            ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "",
                )
                Text(text = "cerrar")
            }
        }
    }
}

@Composable
fun ScreenPreview() {
    Box(Modifier.fillMaxSize()) {
        ShowSwitches([], {})
    }
}