@file:Suppress("UNUSED_EXPRESSION")

package com.capa1.switchcontrol.ui

import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.capa1.switchcontrol.data.Global.MyColors
import com.capa1.switchcontrol.data.model.WeeklyProgram

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
            Surface {
                Box(
                    contentAlignment = Alignment.TopEnd//   .Center
                ) {
                    Column {
                        Text(
                            text = "Agregar interruptor/es",
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(
                            onClick = { addSw() },
                        ) {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                contentDescription = "",
                            )
                            Text(text = "agregar un interruptor nuevo")
                        }
                        TextButton(
                            onClick = { addId()},
                        ) {
                            Icon(
                                Icons.Default.CheckCircleOutline,
                                contentDescription = "",
                            )
                            Text(text = "agregar un interruptor con un Id")
                        }
                        TextButton(
                            onClick = { addAll() },
                        ) {
                            Icon(
                                Icons.Default.FileDownload,
                                contentDescription = "",
                            )
                            Text(text = "enviar/recibir configuración completa")
                        }
                        TextButton(
                            onClick = { onExit() },
                        ) {
                            Icon(
                                Icons.Default.MoodBad,
                                contentDescription = "",
                            )
                            Text(text = " nada de esto por ahora")
                        }

                    }
                }
            }
        }
    }
}
@Composable
fun NewIdDialog( //3: Boolean,
    show: Boolean,
    setId: (String) -> Unit,
    onExit: () -> Unit
) {
    val tokenValue = remember {
        mutableStateOf(TextFieldValue())
    }
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
                            text = "AGREGAR INTERRUPTOR CON UN ID",
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            placeholder = { Text(text = "12 caracteres: 0..9 / a..f") },
                            value = tokenValue.value,
                            singleLine = true,
                            maxLines = 1,
                            onValueChange = {
                                tokenValue.value = it
                            })
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {

                            TextButton(
                                onClick = {
                                    val input = tokenValue.value.text
                                    if (input.length != 12) {
                                        return@TextButton
                                    }
                                    setId(input)
                                },
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
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                )
                                Text(text = "descartar")
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
    val tokenValue = remember {
        mutableStateOf(TextFieldValue())
    }
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
                            text = "COPIAR TODO DE/A OTRO MOVIL",
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Text(
                            text = "Para recibir la configuración completa de otro movil, pasale este código: $allSwId",
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Para enviar la configuración completa a otro movil, anota su código",
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        TextField (
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            placeholder = { Text(text = "12 caracteres: 0..9 / a..f") },
                            value = tokenValue.value,
                            singleLine = true,
                            maxLines = 1,
                            onValueChange = {
                                tokenValue.value = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {
                                    val input = tokenValue.value.text
                                    if (input.length != 12) {
                                        return@TextButton
                                    }
                                    setId(input)
                                },
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
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                )
                                Text(text = "descartar")
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
    val tokenValue = remember {
        mutableStateOf(TextFieldValue())
    }
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
                            text = "CAMBIAR NOMBRE DEL INTERRUPTOR",
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField (
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            placeholder = {
                                Text(
                                    text = " $currentName ",
                                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                )
                            },
                            value = tokenValue.value,
                            singleLine = true,
                            maxLines = 1,
                            onValueChange = {
                                tokenValue.value = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {
                                    val input = tokenValue.value.text
                                    setName(input)
                                },
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
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                )
                                Text(text = "descartar")
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
                            text = "HORARIO INICIAL",
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        var start by remember { mutableStateOf(currentWP.start) }
                        var stop by remember { mutableStateOf(currentWP.stop) }
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
                            text = "HORARIO FINAL",
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
                                    .background(color = if (dom) Color.Blue else Color.LightGray, CircleShape)
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
                                    .background(color = if (lu) Color.Blue else Color.LightGray, CircleShape)
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
                                    .background(color = if (ma) Color.Blue else Color.LightGray, CircleShape)
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
                                    .background(color = if (mi) Color.Blue else Color.LightGray, CircleShape)
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
                                    .background(color = if (ju) Color.Blue else Color.LightGray, CircleShape)
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
                                    .background(color = if (vi) Color.Blue else Color.LightGray, CircleShape)
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
                                    .background(color = if (sa) Color.Blue else Color.LightGray, CircleShape)
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
                                Text(text = "aceptar")
                            }
                            TextButton(
                                onClick = { onExit() },
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                )
                                Text(text = "descartar")
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
                            text = "MODO DE FUNCIONAMIENTO",
                            style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            TextButton(
                                onClick = {

                                },
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
                                    Icons.Default.Delete,
                                    contentDescription = "",
                                )
                                Text(text = "descartar")
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
fun ShowDialog(value: Int = 4) {
    //Column
    when (value) {

        1 -> AddSwDialog(true, {}, {}, {}, {})
        3 -> NewIdDialog(show = true, setId = {}, {})
        4 -> NewAllDialog(true, "tuti", {}, {})
        5 -> NameDialog(true, "luz cocina", {""}, {})
        6 -> ColorDialog(true, "cielo", {},{})
        7 -> TimerDialog(true, WeeklyProgram(2, 3, 1), {}, {})
        8 -> ModeDialog(true) {}
        else -> {}
    }
}