package com.capa1.switchcontrol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.ui.SwViewModel

@Composable
fun ConfigScreen(
    id: String,
    viewModel: SwViewModel = hiltViewModel()
) {
    var currentSwData = SwData(
        "","", 1, "nada", SwStatus.DISCONNECTED
    )
    val screenModifiers by viewModel.screenModifiers.collectAsState()
    for (swData in screenModifiers.swList) {
        if (id == swData.id) {
            currentSwData = swData
        }
    }
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ShowConfig(
            name = currentSwData.name,
            bkColor = currentSwData.bkColor,
            row = currentSwData.row,
            timersInfo = screenModifiers.timersInfo[id] ?: listOf("inactivo",
                "inactivo", "inactivo", "inactivo"),
            exit = {viewModel.exit()},
            process = {viewModel.process()},
            picker = {viewModel.picker()}
        )
    }
}
@Composable
fun ShowConfig(
    name: String,
    bkColor: String,
    row: Int,
    timersInfo: List<String>,
    exit: ()-> Unit,
    process: ()-> Unit,
    picker:()-> Unit
){
    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.Start
        //verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            text = "INTERRUPTOR WiFi",
            style = TextStyle(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "nombre: ",
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = name,
                style = TextStyle(fontSize = 30.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "VISTA",
            style = TextStyle(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.DensityMedium,
                contentDescription = ""
            )
            Text(
                text = "$row",
                style = TextStyle(fontSize = 20.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Icon(
                Icons.Default.Upload,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Icon(
                Icons.Default.Download,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                Icons.Default.ColorLens,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(text = bkColor,
                style = TextStyle(fontSize = 16.sp),
                color = Global.MyColors[bkColor]!!.textColor,
                modifier = Modifier
                    .background(
                        color = Global.MyColors[bkColor]!!.backColor
                    )
                    .clickable { picker() }
                    .padding(all = 8.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "TIMERS",
            style = TextStyle(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = ""
                )
                Text(
                    text = timersInfo[0],
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = ""
                )
                Text(
                    text = timersInfo[1],
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = ""
                )
                Text(
                    text = timersInfo[2],
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = ""
                )
                Text(
                    text = timersInfo[3],
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "CONFIGURACION ADICIONAL",
            style = TextStyle(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Mode,
                contentDescription = ""
            )
            Text(
                text = "modo",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 25.dp))
            Icon(
                Icons.Default.Handyman,
                contentDescription = ""
            )
            Text(
                text = "mantenimiento",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

        }
        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.small)
                //.background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {exit()}) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "",
                )
                Text(text = "abandonar",
                    style = TextStyle(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                    )
            }
            Spacer(modifier = Modifier.padding(horizontal = 25.dp))
            TextButton(onClick = {process()}) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                )
                Text(text = "hecho",
                    style = TextStyle(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ShowConfigPreview()
{
    ShowConfig(
        name = "luz cocina",
        bkColor = "madera",
        row = 1,
        timersInfo = listOf("inactivo", "inactivo", "inactivo", "inactivo"),
        {}, {}, {}
    )
}