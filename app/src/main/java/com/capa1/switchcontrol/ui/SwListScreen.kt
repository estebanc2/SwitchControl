package com.capa1.switchcontrol.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capa1.switchcontrol.data.model.SwData

@Composable
fun MainScreen(
    swViewModel: SwViewModel = hiltViewModel()
) {
    val screenModifiers by swViewModel.screenModifiers.collectAsState()
    Box(Modifier.fillMaxSize()) {
        if (!screenModifiers.showList) {
            ShowSwitches(
                list = swViewModel.getSwList()
            )
        }
    }
}

@Composable
fun ShowSwitches(
    list: List<SwData>
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(
                    text = "Interruptores WiFi",
                    style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier.clickable {  }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(list) { calValue ->
            SwitchRowScreen(calValue, "nada que mostrar", {})
        }
    }
}


@Preview (showBackground = true)
@Composable
fun ScreenPreview() {
    Box(Modifier.fillMaxSize()) {
        ShowSwitches(listOf(SwData("velador", "00AB", 1, 1),
                            SwData("luz cocina", "10AB", 2, 1), ))
    }
}