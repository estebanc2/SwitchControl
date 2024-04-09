package com.capa1.switchcontrol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.capa1.switchcontrol.data.ColorObject

@Composable
fun ColorPickerScreen(
    navController: NavController,
    list: List<ColorObject>
){
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
       items (list) {value ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(color = Color(value.hex.toInt()))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ){
                Text(
                    text = value.name
                )
            }
        }
    }
}
