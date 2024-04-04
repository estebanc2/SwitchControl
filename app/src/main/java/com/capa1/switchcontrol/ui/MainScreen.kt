package com.capa1.switchcontrol.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capa1.switchcontrol.R

@Composable
fun MainScreen(
    viewModel: ViewModel = hiltViewModel()
) {
    val screenModifiers by viewModel.screenModifiers.collectAsState()
    Box(Modifier.fillMaxSize()) {
        if (!screenModifiers.showList) {
            ShowSwitches(
                list = viewModel.getCalList()
            )
        }
    }
}

@Composable
fun ShowSwitches(
    list: List<String>
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            Text(
                text = "Interruptores WiFi",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(list) { calValue ->
            SwitchRow(calValue, "nada que mostrar")
        }
    }
}

@Composable
fun SwitchRow(
    item: String,
    leyenda: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ){
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween){
            Column(){
                Text(
                    text = item,
                    style = TextStyle(fontSize = 24.sp),
                    //color = MaterialTheme.colorScheme.primary
                )
                Text (
                    text = leyenda
                )
            }
            Image(painter = painterResource(id = R.mipmap.open_foreground),
                contentDescription = "",
                Modifier
                    .clickable { }
                    .size(70.dp, 70.dp)
                )
        }
    }
}


@Preview (showBackground = true)
@Composable
fun ScreenPreview() {
    Box(Modifier.fillMaxSize()) {
        ShowSwitches(listOf("agarrate", "boludeame"))
    }
}