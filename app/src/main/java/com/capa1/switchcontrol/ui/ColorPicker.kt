package com.capa1.switchcontrol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capa1.switchcontrol.data.model.ColorList
import com.capa1.switchcontrol.data.model.ColorObject

@Composable
fun ColorPickerScreen(
    list: List<ColorObject>
){
    LazyColumn {
        items (list.) {value ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(color = value.hex)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ){
                Text(
                    text = "rojo"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    ColorPickerScreen(ColorList())
}