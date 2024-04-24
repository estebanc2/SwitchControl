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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MoodBad
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
fun AddSwDialog( //4
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
                            Text(text = "recibir configuración completa")
                        }
                        TextButton(
                            onClick = { addAll() },
                        ) {
                            Icon(
                                Icons.Default.FileUpload,
                                contentDescription = "",
                            )
                            Text(text = "enviar configuración completa")
                        }
                        TextButton(
                            onClick = { onExit() },
                        ) {
                            Icon(
                                Icons.Default.MoodBad,
                                contentDescription = "",
                            )
                            Text(text = "nada de esto por ahora")
                        }

                    }
                }
            }
        }
    }
}
@Composable
fun NameDialog( //1
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
fun ModeDialog( //1
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
@Composable
fun NewIdDialog( //1
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
                            //placeholder = { Text(text = currentName) },
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
                                    if (input.length != 8) {
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
fun ColorDialog( //2
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
fun TimerDialog( //3
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
                            text = "INTERRUPTOR WiFi",
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
                                    Icons.Default.Add,
                                    contentDescription = "",
                                )
                                Text(text = "conectar")
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
fun ShowDialog(value: Int = 5) {
    //Column
    when (value) {

        1 -> NameDialog(true, "luz cocina", {""}, {})
        2 -> ColorDialog(true, "cielo", {},{})
        3 -> TimerDialog(true, WeeklyProgram(2, 3, 1), {}, {})
        4 -> AddSwDialog(true, {}, {}, {}, {})
        5 -> NewIdDialog(show = true, setId = {}, {})
        else -> {}
    }
}