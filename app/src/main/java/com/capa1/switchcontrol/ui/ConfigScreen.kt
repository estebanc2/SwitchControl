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
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwMode
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.data.model.WeeklyProgram

@Composable
fun ConfigScreen(
    qty: Int,
    status: SwStatus,
    data: SwData,
    changeName: () -> Unit,
    changeColor: ()-> Unit,
    changeRow: (Int) -> Unit,
    changeTimer: (Int) -> Unit,
    changeMode: () -> Unit,
    goMaintenance: ()-> Unit,
    save:()->Unit,
    onExit:()->Unit
){
    var row by remember { mutableStateOf(data.row) }
    val prgs by remember { mutableStateOf(data.prgs) }
    fun hours(min: Int): String{
        return "${min/60}:${min - (min/60)*60}"
    }
    fun getBit(value: Int, position: Int): Int {
        return (value shr position) and 1
    }
    fun daysList(day:Int): String{
        var daysIn = ""
        var i = 0
        val dayName = listOf("do, ", "lu, ", "ma, ", "mi, ", "ju, ", "vi, ", "sa, ")
        for (dayString in dayName){
            if(getBit(day, i) != 0){
                daysIn += dayString
            }
            i += 1
        }
        return daysIn
    }
    fun getTimersInfo(prg: WeeklyProgram): String{
        var legend = "inactivo"
        val days = prg.days
        val start = prg.start
        val stop = prg.stop
        if (days != 0){
            legend = "${daysList(days)} de ${hours(start)} a ${hours(stop)}"
        }
        return legend
    }
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
            style = TextStyle(fontSize = 16.sp),
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
                text = data.name,
                style = TextStyle(fontSize = 30.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                Icons.Default.ChangeCircle,
                contentDescription = "",
                modifier = Modifier.clickable { if(status == SwStatus.CONNECTED) changeName() }
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "VISTA",
            style = TextStyle(fontSize = 16.sp),
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
                    .clickable { if(row > 1){
                                    row -= 1
                                    changeRow(-1)
                                }
                    }
            )
            Icon(
                Icons.Default.Download,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
                    .clickable { if(row< qty){
                                    row += 1
                                    changeRow(1)
                                }
                    }
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                Icons.Default.ColorLens,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(text = data.bkColor,
                style = TextStyle(fontSize = 16.sp),
                color = Global.MyColors[data.bkColor]!!.textColor,
                modifier = Modifier
                    .background(
                        color = Global.MyColors[data.bkColor]!!.backColor
                    )
                    .clickable { changeColor() }
                    .padding(all = 8.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "TIMERS",
            style = TextStyle(fontSize = 16.sp),
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
                    contentDescription = "",
                    modifier = Modifier.clickable { if(status == SwStatus.CONNECTED) changeTimer(0)}
                )
                Text(
                    text = getTimersInfo(prgs[0]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .clickable {  if(status == SwStatus.CONNECTED) changeTimer(0) }
                )
            }
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = "",
                    modifier = Modifier.clickable { if(status == SwStatus.CONNECTED) changeTimer(1)}
                )
                Text(
                    text = getTimersInfo(prgs[1]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .clickable {  if(status == SwStatus.CONNECTED) changeTimer(1) }
                )
            }
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = "",
                    modifier = Modifier.clickable { if(status == SwStatus.CONNECTED) changeTimer(2)}
                )
                Text(
                    text = getTimersInfo(prgs[2]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .clickable {  if(status == SwStatus.CONNECTED) changeTimer(2) }
                )
            }
            Row {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = "",
                    modifier = Modifier.clickable { if(status == SwStatus.CONNECTED) changeTimer(3)}
                )
                Text(
                    text = getTimersInfo(prgs[3]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp)
                        .clickable {  if(status == SwStatus.CONNECTED) changeTimer(3) }
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "CONFIGURACION ADICIONAL",
            style = TextStyle(fontSize = 16.sp),
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
                    .clickable {  if(status == SwStatus.CONNECTED) changeMode() }
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
                    .clickable { goMaintenance() }
            )

        }
        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            TextButton(
                onClick = {save()},
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "",
                )
                Text(text = "aceptar")
            }
            TextButton(
                onClick = { onExit() },
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "",
                )
                Text(text = "descartar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowConfigPreview()
{
    ConfigScreen(
        qty = 5,
        status = SwStatus.CONNECTED,
        data = SwData(
            "luz cocina", SwState.OFF, SwMode.TIMERS, 0, Global.NO_TIMERS,
            10, "nada", 2, SwStatus.CONNECTED
        ),
        changeName = {},
        changeColor = {},
        changeRow = {},
        changeTimer = {},
        changeMode = {},
        goMaintenance = {},
        save = {},
        onExit = {}
    )
}