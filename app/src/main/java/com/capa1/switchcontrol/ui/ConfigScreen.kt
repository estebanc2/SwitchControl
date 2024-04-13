package com.capa1.switchcontrol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capa1.switchcontrol.data.model.SwData

@Composable
fun ConfigScreen(
    setSwData: (SwData) -> Unit,
){
    val tokenValue = remember {
        mutableStateOf(TextFieldValue())
    }
    Column(
        Modifier.fillMaxSize(),
        //contentPadding = PaddingValues(all = 20.dp),
    verticalArrangement = Arrangement.spacedBy(5.dp)
    ){
        Text(
            text = "INTERRUPTOR WiFi",
            style = TextStyle(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.small)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "nombre: ",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "luz cocina",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "VISTA",
            style = TextStyle(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.small)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.List,
                contentDescription = ""
            )
            Text(
                text = "2",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = ""
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(horizontal = 70.dp))
            Text(
                text = "cielo",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }

            )
        }
    }
}
/*
@Preview (showBackground = true)
@Composable
fun SwConfigPreview()
{
    ConfigScreen(, {})
}*/