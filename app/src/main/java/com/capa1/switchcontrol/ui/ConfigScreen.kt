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
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Handyman
import androidx.compose.material.icons.rounded.Mode
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.ui.theme.*

@Composable
fun ConfigScreen(
    qty: Int,
    data: SwData,
    changeName: () -> Unit,
    changeIcon: ()-> Unit,
    changeRow: (Int) -> Unit,
    changeTimer: (Int) -> Unit,
    changeMode: () -> Unit,
    goMaintenance: ()-> Unit,
    save:()->Unit,
    onExit:()->Unit
){
    //var row by remember { mutableIntStateOf(data.row) }
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
    @Composable
    fun getTimersInfo(prg: WeeklyProgram): String{
        var legend = stringResource(R.string.inactive)
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
    ) {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            text = stringResource(R.string.configTitle),
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                //.background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.name),
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
                Icons.Rounded.ChangeCircle,
                contentDescription = "",
                tint = AccentColor,
                modifier = Modifier.clickable {  changeName() }
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = stringResource(R.string.look),
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .padding(horizontal = 10.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.DensityMedium,
                tint = AccentColor,
                contentDescription = ""
            )
            Text(
                text = "1",
                style = TextStyle(fontSize = 20.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Icon(
                Icons.Rounded.Upload,
                tint = AccentColor,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        //if (row > 1) {
                            //row -= 1
                            changeRow(-1)
                        //}
                    }
            )
            Icon(
                Icons.Rounded.Download,
                tint = AccentColor,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        //if (row < qty) {
                        //    row += 1
                            changeRow(1)
                        //}
                    }
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                Icons.Rounded.Create,
                tint = AccentColor,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(text = data.icon,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                  //  .background(
                    //    color = Global.MyColors[data.icon]!!.backColor
                    //)
                    .clickable { changeIcon() }
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
                //.background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row {
                Icon(
                    Icons.Rounded.Restore,
                    tint = AccentColor,
                    contentDescription = "",
                    modifier = Modifier.clickable { changeTimer(0)}
                )
                Text(
                    text = getTimersInfo(data.prgs[0]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable { changeTimer(0) }
                )
            }
            Row {
                Icon(
                    Icons.Rounded.Restore,
                    contentDescription = "",
                    tint = AccentColor,
                    modifier = Modifier.clickable {  changeTimer(1)}
                )
                Text(
                    text = getTimersInfo(data.prgs[1]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable { changeTimer(1) }
                )
            }
            Row {
                Icon(
                    Icons.Rounded.Restore,
                    tint = AccentColor,
                    contentDescription = "",
                    modifier = Modifier.clickable { changeTimer(2)}
                )
                Text(
                    text = getTimersInfo(data.prgs[2]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable {  changeTimer(2) }
                )
            }
            Row {
                Icon(
                    Icons.Rounded.Restore,
                    tint = AccentColor,
                    contentDescription = "",
                    modifier = Modifier.clickable {  changeTimer(3)}
                )
                Text(
                    text = getTimersInfo(data.prgs[3]),
                    style = TextStyle(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable {  changeTimer(3) }
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = stringResource(R.string.moreConfig),
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                //.background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Mode,
                tint = AccentColor,
                contentDescription = ""
            )
            Text(
                text = stringResource(R.string.mode),
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {  changeMode() }
            )
            Spacer(modifier = Modifier.padding(horizontal = 25.dp))
            Icon(
                Icons.Rounded.Handyman,
                tint = AccentColor,
                contentDescription = ""
            )
            Text(
                text = stringResource(R.string.maintenance),
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable { goMaintenance() }
            )

        }
        Spacer(modifier = Modifier.padding(vertical = 25.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            TextButton(
                onClick = {save()},
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    tint = AccentColor,
                    contentDescription = "",
                )
                Text(stringResource(R.string.accept))
            }
            TextButton(
                onClick = { onExit() },
            ) {
                Icon(
                    Icons.Rounded.Close,
                    tint = AccentColor,
                    contentDescription = "",
                )
                Text(stringResource(R.string.noAccept))
            }
        }
    }
}
