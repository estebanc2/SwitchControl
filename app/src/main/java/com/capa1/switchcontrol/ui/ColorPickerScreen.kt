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
import com.capa1.switchcontrol.data.ColorObject

@Composable
fun ColorPickerScreen(
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

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    val blackHex = "0xFF000000"
    val whiteHex = "0xFFFFFFFF"
    ColorPickerScreen(listOf(
        ColorObject("Black", blackHex, whiteHex),
        ColorObject("Silver", "C0C0C0", blackHex),
        ColorObject("Gray", "808080", whiteHex),
        ColorObject("Maroon", "800000", whiteHex),
        ColorObject("Red", "FF0000", whiteHex),
        ColorObject("Fuchsia", "FF00FF", whiteHex),
        ColorObject("Green", "008000", whiteHex),
        ColorObject("Lime", "00FF00", blackHex),
        ColorObject("Olive", "808000", whiteHex),
        ColorObject("Yellow", "FFFF00", blackHex),
        ColorObject("Navy", "000080", whiteHex),
        ColorObject("Blue", "0000FF", whiteHex),
        ColorObject("Teal", "008080", whiteHex),
        ColorObject("Aqua", "00FFFF", blackHex)
    ))
}