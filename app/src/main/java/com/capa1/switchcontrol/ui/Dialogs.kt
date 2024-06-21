@file:Suppress("UNUSED_EXPRESSION")

package com.capa1.switchcontrol.ui
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.PhonelinkErase
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.ESPTOUCH_WAIT_IN_SECS
import com.capa1.switchcontrol.data.Global.MyColors
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.SwMode
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.data.wifi.ApData
import com.capa1.switchcontrol.data.wifi.TouchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NoPermissionDialog( //0
    show: Boolean, onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(onDismissRequest = {}, confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = "ok",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }
        }, text = {
            Text(stringResource(R.string.permissionLegend),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
        })
    }
}
@Composable
fun AddSwDialog( //1
    show: Boolean,
    addSw:()->Unit,
    addId:()->Unit,
    addAll:()->Unit,
    onExit: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd//   .Center
                ) {
                    Column (
                        modifier = Modifier.padding(10.dp)
                    ){
                        Text(
                            text = stringResource(R.string.menuTitle),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                        TextButton(
                            onClick = { addSw() },
                        ) {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                contentDescription = "",
                            )
                            Text(stringResource(R.string.addNew),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 0.dp))
                        }
                        TextButton(
                            onClick = { addId()},
                        ) {
                            Icon(
                                Icons.Default.CheckCircleOutline,
                                contentDescription = "",
                            )
                            Text(stringResource(R.string.addWithId),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 0.dp))
                        }
                        TextButton(
                            onClick = { addAll() },
                        ) {
                            Icon(
                                Icons.Default.FileDownload,
                                contentDescription = "",
                            )
                            Text(stringResource(R.string.wholeConfig),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 0.dp))
                        }
                        var showHow by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = { showHow = true },
                        ) {
                            Icon(
                                Icons.Default.Mode,
                                contentDescription = "",
                            )
                            Text(stringResource(R.string.configSw),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 0.dp))
                        }
                        if(showHow){
                            Text(
                                text = stringResource(R.string.touchToConfig),
                                style = TextStyle(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                            ElevatedButton(onClick = {showHow = false}){
                                Text(text = stringResource(R.string.gotIt))}
                        }
                        TextButton(
                            onClick = { onExit() },
                        ) {
                            Icon(
                                Icons.Default.MoodBad,
                                contentDescription = ""
                             )
                            Text(text = stringResource(R.string.nothing),
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 0.dp))
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun NewDialog( //2
    show: Boolean,
    apData: ApData,
    state: TouchState,
    setPass: (String) -> Unit,
    onExit: () -> Unit
) {
    var pass by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.addNewTitle),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,

                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if(apData.is5G) {
                            Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.WarningAmber,
                                    contentDescription = "",
                                    tint = Color.Red
                                )
                                Text(
                                    text = stringResource(R.string.msg5Gh),
                                    style = TextStyle(fontSize = 14.sp),
                                    color = Color.Red,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 0.dp
                                    ),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row (Modifier, verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    text = "ssid: ",
                                    style = TextStyle(fontSize = 18.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                Text(
                                    text = apData.ssid,
                                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                                )
                            }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row (Modifier, verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = "clave: ",
                                style = TextStyle(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary,
                            )
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                value = pass,
                                singleLine = true,
                                maxLines = 1,
                                onValueChange = { pass = it }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        var showProgress by remember { mutableStateOf(false) }
                        var currentProgress by remember { mutableFloatStateOf(0f) }
                        val scope = rememberCoroutineScope() // Create a coroutine scope
                        val job: Job? by remember { mutableStateOf(null) }
                        LaunchedEffect (showProgress){
                            scope.launch {
                                loadProgress { progress ->
                                    currentProgress = progress
                                }
                                Log.i(TAG,"1")
                            }

                        }
                        if(showProgress){
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                progress = { currentProgress }
                            )
                        }
                        if(state == TouchState.TIMEOUT){
                            job?.cancel()
                            showProgress = false
                            currentProgress = 0f
                            Text(
                                text = stringResource(R.string.badKey),
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                            )
                        } else if (state == TouchState.READY) { onExit() }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {
                                    currentProgress = 0f
                                    setPass(pass)
                                    showProgress = true
                                },
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.accept))
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    val max = ESPTOUCH_WAIT_IN_SECS * 1000 / 100
    for (i in 1..max) {
        updateProgress(i.toFloat() / max)
        delay(100)
    }
}

@Composable
fun NewIdDialog( //3: Boolean,
    show: Boolean,
    setId: (String) -> Unit,
    onExit: () -> Unit
) {
     if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.addIdTitle),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        var id by remember { mutableStateOf("") }

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            placeholder = { Text(stringResource(R.string.validChar)) },
                            value = id,
                            singleLine = true,
                            maxLines = 1,
                            onValueChange = {id = it}
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {

                            TextButton(
                                onClick = {
                                    if (id.length != 12) {
                                        return@TextButton
                                    }
                                    setId(id)
                                },
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.accept))
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun NewAllDialog( //4
    show: Boolean,
    allSwId: String,
    setId: (String) -> Unit,
    onExit: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        //verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.allTitle),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        )
                        Text(
                            text = stringResource(R.string.wholeConfigReceive, allSwId),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Text(
                            text = stringResource(R.string.wholeConfigSend),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        )
                        var id by remember { mutableStateOf("") }
                        TextField (
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            placeholder = { Text(stringResource(R.string.validChar)) },
                            value = id,
                            singleLine = true,
                            maxLines = 1,
                            onValueChange = { id = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {
                                    if (id.length != 12) {
                                        return@TextButton
                                    }
                                    setId(id)
                                },
                            ) {
                                Icon(
                                    Icons.Default.Upload,
                                    contentDescription = "",
                                )
                                Text(text = stringResource(R.string.send))
                            }
                            TextButton(
                                onClick = {setId("0")},
                            ) {
                                Icon(
                                    Icons.Default.Download,
                                    contentDescription = "",
                                )
                                Text(text = stringResource(R.string.receive))
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun NameDialog( //5
    show: Boolean,
    currentName: String,
    setName: (String) -> Unit,
    onExit: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.changeNameTitle),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        var name by remember { mutableStateOf(currentName) }
                        TextField (
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            value = name,
                            singleLine = true,
                            maxLines = 1,
                            onValueChange = { name = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {

                                    setName(name)
                                },
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.accept))
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ColorDialog( //6
    show: Boolean,
    currentColor: String,
    setColor: (String) -> Unit,
    exit:()->Unit
) {
    val colorsList = MyColors.entries.toList()
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(all = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(colorsList){ data->
                            Row {
                              Box(
                                  contentAlignment = Alignment.Center,
                                  modifier = Modifier
                                      .size(70.dp, 30.dp)
                                      .background(
                                          color = data.value.backColor
                                      )
                              ){
                                  Text(
                                      text = data.key,
                                      style = TextStyle(fontSize = 16.sp),
                                      color = data.value.textColor,
                                      modifier = Modifier
                                          .clickable {
                                              setColor(data.key)
                                              exit()
                                          }
                                          .padding(all = 3.dp)
                                  )
                              }
                              if(data.key == currentColor){
                                  Icon(
                                      Icons.Default.Check,
                                      contentDescription = "",
                                  )
                              }
                          }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TimerDialog( //7
    show: Boolean,
    currentWP: WeeklyProgram,
    setTimer: (WeeklyProgram) -> Unit,
    onExit: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                         Text(
                            text = stringResource(R.string.startTime),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        var start by remember { mutableIntStateOf(currentWP.start) }
                        var stop by remember { mutableIntStateOf(currentWP.stop) }
                        fun checkMinStop(){
                            if(stop <= start){
                                stop = start + 1
                            }
                        }
                        fun getMinutes(min: Int): Int{
                            return min - (min/60)*60
                        }
                        Row(Modifier, verticalAlignment = Alignment.CenterVertically){
                            Text(
                                    text = "hora: ${start/60}",
                                    style = TextStyle(fontSize = 20.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                                )
                            Column{
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "",
                                    Modifier.clickable {
                                        if(start < 23*60) start += 60
                                        checkMinStop()
                                    }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "",
                                    Modifier.clickable { if(start > 60) start -= 60 }
                                )
                            }
                            Text(
                                text = "min.: ${getMinutes(start)}",
                                style = TextStyle(fontSize = 20.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            )
                            Column{
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "",
                                    Modifier.clickable {
                                        if(getMinutes(start) < 59) start += 1
                                        checkMinStop()
                                    }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "",
                                    Modifier.clickable { if(getMinutes(start) > 0) start -= 1 }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.finalTime),
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Row(Modifier, verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = "hora: ${stop/60}",
                                style = TextStyle(fontSize = 20.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp))
                            Column{
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "",
                                    Modifier.clickable { if(stop < 23*60) stop += 60 }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "",
                                    Modifier.clickable {
                                        if(stop > 60) stop -= 60
                                        checkMinStop()
                                    }
                                )
                            }
                            Text(
                                text = "min.: ${getMinutes(stop)}",
                                style = TextStyle(fontSize = 20.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            )
                            Column{
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "",
                                    Modifier.clickable { if(getMinutes(stop) < 59) stop += 1 }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "",
                                    Modifier.clickable {
                                        if(getMinutes(stop) > 0) stop -= 1
                                        checkMinStop()
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "DIAS",
                            style = TextStyle(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        fun isSet( position: Int): Boolean {
                            return currentWP.days shr position and 1 == 1
                        }
                        var dom by remember { mutableStateOf(isSet(0)) }
                        var lu  by remember { mutableStateOf(isSet(1)) }
                        var ma  by remember { mutableStateOf(isSet(2)) }
                        var mi  by remember { mutableStateOf(isSet(3)) }
                        var ju  by remember { mutableStateOf(isSet(4)) }
                        var vi  by remember { mutableStateOf(isSet(5)) }
                        var sa  by remember { mutableStateOf(isSet(6)) }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly){
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (dom) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { dom = !dom },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "do",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (lu) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { lu = !lu },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "lu",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (ma) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { ma = !ma },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "ma",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (mi) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { mi = !mi },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "mi",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (ju) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { ju = !ju },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "ju",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (vi) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { vi = !vi },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "vi",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (sa) Color.Blue else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable { sa = !sa },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "sa",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            fun Boolean.toInt() = if (this) 1 else 0
                            val days = dom.toInt()+lu.toInt()*2+ma.toInt()*4+mi.toInt()*8+ju.toInt()*16+vi.toInt()*32+sa.toInt()*64
                            TextButton(
                                onClick = {setTimer(WeeklyProgram(start, stop, days))
                            },
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.accept))
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ModeDialog( //8
    show: Boolean,
    currentMode:SwMode,
    currentSecs: Int,
    setMode: (Pair<SwMode,Int>) -> Unit,
    onExit: () -> Unit
) {

    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.modeTitle),
                            style = TextStyle(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        var secs by remember { mutableIntStateOf(currentSecs) }
                        var mode by remember { mutableStateOf(currentMode) }
                        val options : MutableList<String> = mutableListOf()
                        for (eachMode in SwMode.entries){
                            options += eachMode.name
                        }
                        options.forEachIndexed { index, label ->
                            val visible = if (index != mode.ordinal) Color(0xFFEEEEEA)
                                        else MaterialTheme.colorScheme.primary
                            Column {
                                Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "",
                                        tint = visible
                                    )
                                    Text(label,
                                        style = TextStyle(fontSize = 18.sp),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 8.dp,
                                                vertical = 8.dp
                                            )
                                            .clickable {
                                                mode = SwMode.entries.toTypedArray()[index]
                                            }
                                    )
                                    if ((index == 2) || (index == 3)) {
                                        Text(
                                            text = stringResource(R.string.sec, secs),
                                            style = TextStyle(fontSize = 18.sp),
                                            color = visible,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 0.dp
                                            ),
                                        )
                                        Column {
                                            Icon(
                                                Icons.Default.KeyboardArrowUp,
                                                contentDescription = "",
                                                tint = visible,
                                                modifier = Modifier.clickable { if (secs < 250) secs += 1 }
                                            )
                                            Icon(
                                                Icons.Default.KeyboardArrowDown,
                                                contentDescription = "",
                                                tint = visible,
                                                modifier = Modifier.clickable { if (secs > 0) secs -= 1 }
                                            )
                                        }
                                    } else if ((index == 4) || (index == 5)) {
                                        Text(
                                            text = stringResource(R.string.grad, secs / 10),
                                            style = TextStyle(fontSize = 18.sp),
                                            color = visible,
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 0.dp
                                            ),
                                        )
                                        Column {
                                            Icon(
                                                Icons.Default.KeyboardArrowUp,
                                                contentDescription = "",
                                                tint = visible,
                                                modifier = Modifier.clickable { if (secs < 220) secs += 10 }
                                            )
                                            Icon(
                                                Icons.Default.KeyboardArrowDown,
                                                contentDescription = "",
                                                tint = visible,
                                                modifier = Modifier.clickable { if (secs > 0) secs -= 10 }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(onClick = { setMode(Pair(mode, secs))},
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.accept))
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MaintenanceDialog( //9
    show: Boolean,
    id: String,
    upgrading: Int,
    lastServer: String,
    lastPort: String,
    upgrade: (Pair<String, String>) -> Unit,
    name:String,
    local: ()->Unit,
    full: ()->Unit,
    onExit: () -> Unit
) {
    var showProgress by remember { mutableStateOf(false) }
    var server by remember { mutableStateOf(lastServer) }
    var port by remember { mutableStateOf(lastPort) }
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.idToReceive),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 0.dp,
                            top = 10.dp, end = 0.dp, bottom = 0.dp),
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = MaterialTheme.shapes.medium)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp))
                        {
                            Row (Modifier, verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    text = "ID: ",
                                    style = TextStyle(fontSize = 18.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                                )
                                SelectionContainer {
                                    Text(
                                        text = id,
                                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = stringResource(R.string.firmwareUpgradeTitle),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 0.dp,
                                top = 15.dp, end = 0.dp, bottom = 0.dp),
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = MaterialTheme.shapes.medium)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp))
                        {
                            Row (Modifier, verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    text = stringResource(R.string.server),
                                    style = TextStyle(fontSize = 18.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 0.dp,
                                        top = 0.dp, end = 0.dp, bottom = 0.dp),
                                )
                                TextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                    value = server,
                                    singleLine = true,
                                    maxLines = 1,
                                    onValueChange = { server = it }
                                )
                            }
                            Row (Modifier, verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    text = stringResource(R.string.port),
                                    style = TextStyle(fontSize = 18.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 0.dp,
                                        top = 0.dp, end = 10.dp, bottom = 0.dp),
                                )
                                TextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                                    value = port,
                                    singleLine = true,
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    onValueChange = { port = it }
                                )
                            }
                            when(upgrading){
                                SwState.UPGRADE.ordinal->{
                                    showProgress = true
                                }
                                SwState.SERVER_FAIL.ordinal->{
                                    Text(
                                        text = stringResource(R.string.badPortServer),
                                        style = TextStyle(fontSize = 16.sp),
                                        color = Color.Red,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                    )
                                    showProgress = false
                                }
                                SwState.UPGRADE_FAIL.ordinal->{
                                    Text(
                                        text = stringResource(R.string.upgradeFails),
                                        style = TextStyle(fontSize = 16.sp),
                                        color = Color.Red,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                    )
                                    showProgress = false
                                }
                                SwState.UPGRADED.ordinal->{
                                    Text(
                                        text = stringResource(R.string.upgradeSuccess),
                                        style = TextStyle(fontSize = 16.sp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                    )
                                    showProgress = false
                                }
                                else->{}
                            }
                            if(showProgress) {
                                LinearProgressIndicator(
                                    //progress = 0.7f,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                            ElevatedButton(
                                onClick = { upgrade(Pair(server, port)) },
                            ) {
                                Icon(
                                    Icons.Default.Upgrade,
                                    contentDescription = "",
                                )
                                Text(text = stringResource(R.string.upgrade))
                            }

                        }
                        Text(
                            text = stringResource(R.string.eraseTitle),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 0.dp,
                                top = 15.dp, end = 0.dp, bottom = 0.dp),
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = MaterialTheme.shapes.medium)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp))
                        {
                            var erasing by remember { mutableStateOf(false)}
                            ElevatedButton(onClick = { erasing = true}) {
                                Icon(
                                      Icons.Default.PhonelinkErase,
                                            contentDescription = "",
                                )
                                Text(
                                    text = stringResource(R.string.localErase, name),
                                    style = TextStyle(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(
                                            start = 5.dp,
                                            top = 0.dp, end = 0.dp, bottom = 0.dp
                                        )
                                )
                            }
                            if (erasing){
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text(
                                        text = stringResource(R.string.sure),
                                        style = TextStyle(fontSize = 14.sp),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Button(
                                        onClick = { local()},
                                    ) {
                                        Text(stringResource(R.string.yes))
                                    }
                                    Button(
                                        onClick = { erasing = false },
                                    ) {
                                        Text(stringResource(R.string.no))
                                    }
                                }
                            }
                            var fullErasing by remember { mutableStateOf(false)}
                            ElevatedButton(onClick = { fullErasing = true}){
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                )
                                Text(
                                    text = stringResource(R.string.fullErase, name),
                                    style = TextStyle(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(
                                            start = 5.dp,
                                            top = 0.dp, end = 0.dp, bottom = 0.dp
                                        )
                                )
                            }
                            if (fullErasing){
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text(
                                        text = stringResource(R.string.sure),
                                        style = TextStyle(fontSize = 14.sp),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Button(
                                        onClick = { full()},
                                    ) {
                                        Text(stringResource(R.string.yes))
                                    }
                                    Button(
                                        onClick = { fullErasing = false },
                                    ) {
                                        Text(stringResource(R.string.no))
                                    }
                                }
                            }

                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {

                                },
                            ) {

                            }
                            TextButton(
                                onClick = {
                                    showProgress = false
                                    onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                )
                                Text(stringResource(R.string.noAccept))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun ShowDialog(value: Int = 9) {
    //Column
    when (value) {

        1 -> AddSwDialog(true, {}, {}, {}, {})
        2 -> NewDialog(true,ApData("myHome", "",false), TouchState.IN_PROGRESS, {""}, {})
        3 -> NewIdDialog(show = true, setId = {}, {})
        4 -> NewAllDialog(true, "all", {}, {})
        5 -> NameDialog(true, "kitchen light", {""}, {})
        6 -> ColorDialog(true, "metal", {},{})
        7 -> TimerDialog(true, WeeklyProgram(2, 3, 1), {}, {})
        8 -> ModeDialog(true, SwMode.TEMP, 10, {  }, {})
        9 -> MaintenanceDialog(true, "1234", SwState.SERVER_FAIL.ordinal, "", "", {},"kitchen light", {},{},{})
    }
}