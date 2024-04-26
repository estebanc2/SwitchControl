package com.capa1.switchcontrol.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
            Surface(
                //shape = RoundedCornerShape(16.dp), color = Color(0xFFEEEEEA)
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd//   .Center
                ) {
                    Column(
                        //horizontalAlignment  =  Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Agregar interruptor/es",
                            style = TextStyle(fontSize = 16.sp),
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
                            style = TextStyle(fontSize = 10.sp),
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
                            style = TextStyle(fontSize = 20.sp),
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
                            style = TextStyle(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            placeholder = { Text(text = currentName) },
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
                                    if (input.length != 4) {
                                        return@TextButton
                                    }
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
    currentTimer: WeeklyProgram,
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
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                         Text(
                            text = "AHORARIO INICIAL",
                            style = TextStyle(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Row{
                            Text(
                                text = "hora: ${currentTimer.start/60}",
                                style = TextStyle(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            )
                            Text(
                                text = "minutos: ${currentTimer.start - (currentTimer.start/60)*60}",
                                style = TextStyle(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "HORARIO FINAL",
                            style = TextStyle(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        Row{
                            Text(
                                text = "hora: ${currentTimer.stop/60}",
                                style = TextStyle(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            )
                            Text(
                                text = "minutos: ${currentTimer.stop - (currentTimer.start/60)*60}",
                                style = TextStyle(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "DIAS",
                            style = TextStyle(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                        )
                        var days = 0
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly){
                           Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                                    .background(Color.LightGray, CircleShape)
                                    .clickable {days += 128
                                    },
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
                            text = "CAMBIAR NOMBRE DEL INTERRUPTOR",
                            style = TextStyle(fontSize = 10.sp),
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
fun ShowDialog(value: Int = 7) {
    //Column
    when (value) {

        1 -> AddSwDialog(true, {}, {}, {}, {})
        3 -> NewIdDialog(show = true, setId = {}, {})
        4 -> NewAllDialog(true, "tuti", {}, {})
        5 -> NameDialog(true, "luz cocina", {""}, {})
        6 -> ColorDialog(true, "cielo", {},{})
        7 -> TimerDialog(true, WeeklyProgram(2, 3, 1), {}, {})
        8 -> ModeDialog(true, {})
        else -> {}
    }
}