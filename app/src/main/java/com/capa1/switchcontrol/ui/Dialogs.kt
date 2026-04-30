@file:Suppress("UNUSED_EXPRESSION")

package com.capa1.switchcontrol.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Mode
import androidx.compose.material.icons.rounded.MoodBad
import androidx.compose.material.icons.rounded.PhonelinkErase
import androidx.compose.material.icons.rounded.Upgrade
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material.icons.rounded.WarningAmber
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
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.IconMapper
import com.capa1.switchcontrol.data.model.IconMapper.fromName
import com.capa1.switchcontrol.data.model.Mode
import com.capa1.switchcontrol.data.model.State
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.data.wifi.ApData
import com.capa1.switchcontrol.data.wifi.TouchState
import com.capa1.switchcontrol.ui.theme.AccentColor
import com.capa1.switchcontrol.ui.theme.BgCard
import com.capa1.switchcontrol.ui.theme.TextPrimary
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ── Helpers de tema ───────────────────────────────────────────────────────────
// Surface base para todos los diálogos: fondo BgCard + esquinas redondeadas
@Composable
private fun DialogSurface(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BgCard
    ) {
        content()
    }
}

// Fila de acciones Aceptar / Cancelar reutilizable
@Composable
private fun DialogActions(
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    acceptLabel: @Composable () -> Unit = { Text(stringResource(R.string.accept)) },
    cancelLabel: @Composable () -> Unit = { Text(stringResource(R.string.noAccept)) },
    acceptIcon: @Composable () -> Unit = {
        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = AccentColor)
    },
    cancelIcon: @Composable () -> Unit = {
        Icon(Icons.Rounded.Close, contentDescription = null, tint = AccentColor)
    }
) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        TextButton(onClick = onCancel) {
            cancelIcon()
            Spacer(Modifier.width(4.dp))
            cancelLabel()
        }
        TextButton(onClick = onAccept) {
            acceptIcon()
            Spacer(Modifier.width(4.dp))
            acceptLabel()
        }
    }
}

// ── 0 NoPermissionDialog ──────────────────────────────────────────────────────
@Composable
fun NoPermissionDialog(
    show: Boolean,
    onConfirm: () -> Unit
) {
    if (!show) return
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("ok", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Text(
                stringResource(R.string.permissionLegend),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

// ── 1 AddSwDialog ─────────────────────────────────────────────────────────────
@Composable
fun AddSwDialog(
    show: Boolean,
    addSw: () -> Unit,
    addId: () -> Unit,
    addAll: () -> Unit,
    onExit: () -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.menuTitle),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                TextButton(onClick = addSw) {
                    Icon(Icons.Rounded.AddCircleOutline, contentDescription = null, tint = AccentColor)
                    Text(stringResource(R.string.addNew), modifier = Modifier.padding(horizontal = 10.dp))
                }
                TextButton(onClick = addId) {
                    Icon(Icons.Rounded.CheckCircleOutline, contentDescription = null, tint = AccentColor)
                    Text(stringResource(R.string.addWithId), modifier = Modifier.padding(horizontal = 10.dp))
                }
                TextButton(onClick = addAll) {
                    Icon(Icons.Rounded.FileDownload, contentDescription = null, tint = AccentColor)
                    Text(stringResource(R.string.wholeConfig), modifier = Modifier.padding(horizontal = 10.dp))
                }
                var showHow by remember { mutableStateOf(false) }
                TextButton(onClick = { showHow = true }) {
                    Icon(Icons.Rounded.Mode, contentDescription = null, tint = AccentColor)
                    Text(stringResource(R.string.configSw), modifier = Modifier.padding(horizontal = 10.dp))
                }
                if (showHow) {
                    Text(
                        text = stringResource(R.string.touchToConfig),
                        style = TextStyle(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                    ElevatedButton(onClick = { showHow = false }) {
                        Text(stringResource(R.string.gotIt))
                    }
                }
                TextButton(onClick = onExit) {
                    Icon(Icons.Rounded.MoodBad, contentDescription = null, tint = AccentColor)
                    Text(stringResource(R.string.nothing), modifier = Modifier.padding(horizontal = 10.dp))
                }
            }
        }
    }
}

// ── 2 NewDialog ───────────────────────────────────────────────────────────────
@Composable
fun NewDialog(
    show: Boolean,
    apData: ApData,
    state: TouchState,
    setPass: (String) -> Unit,
    onExit: () -> Unit
) {
    var pass by remember { mutableStateOf("") }
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.addNewTitle),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (apData.is5G) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = Color.Red)
                        Text(
                            text = stringResource(R.string.msg5Gh),
                            style = TextStyle(fontSize = 14.sp),
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ssid: ", style = TextStyle(fontSize = 18.sp), color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = apData.ssid,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("clave: ", style = TextStyle(fontSize = 18.sp), color = MaterialTheme.colorScheme.primary)
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
                val scope = rememberCoroutineScope()
                val job: Job? by remember { mutableStateOf(null) }
                LaunchedEffect(showProgress) {
                    scope.launch {
                        loadProgress { progress -> currentProgress = progress }
                        Log.i(TAG, "1")
                    }
                }
                if (showProgress) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        progress = { currentProgress }
                    )
                }
                if (state == TouchState.TIMEOUT) {
                    job?.cancel()
                    showProgress = false
                    currentProgress = 0f
                    Text(
                        text = stringResource(R.string.badKey),
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )
                } else if (state == TouchState.READY) {
                    onExit()
                }
                Spacer(modifier = Modifier.height(10.dp))
                DialogActions(
                    onAccept = {
                        currentProgress = 0f
                        setPass(pass)
                        showProgress = true
                    },
                    onCancel = onExit
                )
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

// ── 3 NewIdDialog ─────────────────────────────────────────────────────────────
@Composable
fun NewIdDialog(
    show: Boolean,
    setId: (String) -> Unit,
    onExit: () -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.addIdTitle),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
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
                    onValueChange = { id = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                DialogActions(
                    onAccept = { if (id.length == 12) setId(id.lowercase()) },
                    onCancel = onExit
                )
            }
        }
    }
}

// ── 4 NewAllDialog ────────────────────────────────────────────────────────────
@Composable
fun NewAllDialog(
    show: Boolean,
    allSwId: String,
    setId: (String) -> Unit,
    onExit: () -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.allTitle),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                Text(
                    text = stringResource(R.string.wholeConfigReceive, allSwId),
                    style = TextStyle(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = stringResource(R.string.wholeConfigSend),
                    style = TextStyle(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
                var id by remember { mutableStateOf("") }
                TextField(
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
                    TextButton(onClick = { if (id.length == 12) setId(id) }) {
                        Icon(Icons.Rounded.Upload, contentDescription = null, tint = AccentColor)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.send))
                    }
                    TextButton(onClick = { setId("0") }) {
                        Icon(Icons.Rounded.Download, contentDescription = null, tint = AccentColor)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.receive))
                    }
                    TextButton(onClick = onExit) {
                        Icon(Icons.Rounded.Close, contentDescription = null, tint = AccentColor)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.noAccept))
                    }
                }
            }
        }
    }
}

// ── 5 NameDialog ──────────────────────────────────────────────────────────────
@Composable
fun NameDialog(
    show: Boolean,
    currentName: String,
    setName: (String) -> Unit,
    onExit: () -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.changeNameTitle),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                var name by remember { mutableStateOf(currentName) }
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    value = name,
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = { name = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                DialogActions(onAccept = { setName(name) }, onCancel = onExit)
            }
        }
    }
}

// ── 6 IconDialog ──────────────────────────────────────────────────────────────
@Composable
fun IconDialog(
    show: Boolean,
    currentIcon: String,
    setIcon: (String) -> Unit,
    onExit: () -> Unit
) {
    if (!show) return

    // Estado DENTRO del bloque: se reinicializa cada vez que el diálogo se abre
    var iconName by remember(currentIcon) { mutableStateOf(currentIcon) }

    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {

                Text(
                    text = stringResource(R.string.changeIconTitle), // o el string que uses
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Grilla de íconos
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    // Agrupa de a 4 por fila
                    val chunked = IconMapper.names.chunked(4)
                    items(chunked) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowItems.forEach { name ->
                                val isSelected = name == iconName
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) AccentColor.copy(alpha = 0.15f)
                                            else Color.Transparent
                                        )
                                        .clickable { iconName = name },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = fromName(name),
                                        contentDescription = null,
                                        tint = if (isSelected) AccentColor else Color.Gray,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            // Rellena celdas vacías para mantener el layout
                            repeat(4 - rowItems.size) {
                                Spacer(modifier = Modifier.size(56.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                DialogActions(
                    onAccept = { setIcon(iconName); onExit() },
                    onCancel = onExit
                )
            }
        }
    }
}

// ── 7 TimerDialog ─────────────────────────────────────────────────────────────
@Composable
fun TimerDialog(
    show: Boolean,
    currentWP: WeeklyProgram,
    setTimer: (WeeklyProgram) -> Unit,
    onExit: () -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(R.string.startTime),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                var start by remember { mutableIntStateOf(currentWP.start) }
                var stop  by remember { mutableIntStateOf(currentWP.stop)  }
                fun checkMinStop() { if (stop <= start) stop = start + 1 }
                fun getMinutes(min: Int) = min - (min / 60) * 60

                // Hora inicio
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "hora: ${start / 60}",
                        style = TextStyle(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column {
                        Icon(Icons.Rounded.KeyboardArrowUp,   contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (start < 23 * 60) { start += 60; checkMinStop() } })
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (start > 60) start -= 60 })
                    }
                    Text(
                        text = "min.: ${getMinutes(start)}",
                        style = TextStyle(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column {
                        Icon(Icons.Rounded.KeyboardArrowUp,   contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (getMinutes(start) < 59) { start += 1; checkMinStop() } })
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (getMinutes(start) > 0) start -= 1 })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.finalTime),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Hora fin
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "hora: ${stop / 60}",
                        style = TextStyle(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column {
                        Icon(Icons.Rounded.KeyboardArrowUp,   contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (stop < 23 * 60) stop += 60 })
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (stop > 60) { stop -= 60; checkMinStop() } })
                    }
                    Text(
                        text = "min.: ${getMinutes(stop)}",
                        style = TextStyle(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Column {
                        Icon(Icons.Rounded.KeyboardArrowUp,   contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (getMinutes(stop) < 59) stop += 1 })
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = AccentColor,
                            modifier = Modifier.clickable { if (getMinutes(stop) > 0) { stop -= 1; checkMinStop() } })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "DIAS",
                    style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                fun isSet(position: Int) = currentWP.days shr position and 1 == 1
                var dom by remember { mutableStateOf(isSet(0)) }
                var lu  by remember { mutableStateOf(isSet(1)) }
                var ma  by remember { mutableStateOf(isSet(2)) }
                var mi  by remember { mutableStateOf(isSet(3)) }
                var ju  by remember { mutableStateOf(isSet(4)) }
                var vi  by remember { mutableStateOf(isSet(5)) }
                var sa  by remember { mutableStateOf(isSet(6)) }

                // Selector de días
                val days  = listOf("do", "lu", "ma", "mi", "ju", "vi", "sa")
                val states = listOf(dom, lu, ma, mi, ju, vi, sa)
                val setters: List<(Boolean) -> Unit> = listOf(
                    { dom = it }, { lu = it }, { ma = it }, { mi = it },
                    { ju = it }, { vi = it }, { sa = it }
                )
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                    days.forEachIndexed { i, label ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    color = if (states[i]) AccentColor else Color.White.copy(alpha = 0.15f),
                                    shape = CircleShape
                                )
                                .clickable { setters[i](!states[i]) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                fun Boolean.toInt() = if (this) 1 else 0
                val daysValue = dom.toInt() + lu.toInt()*2 + ma.toInt()*4 + mi.toInt()*8 +
                        ju.toInt()*16 + vi.toInt()*32 + sa.toInt()*64
                DialogActions(
                    onAccept = { setTimer(WeeklyProgram(start, stop, daysValue)) },
                    onCancel = onExit
                )
            }
        }
    }
}

// ── 8 ModeDialog ──────────────────────────────────────────────────────────────
@Composable
fun ModeDialog(
    show: Boolean,
    currentMode: Mode,
    currentSecs: Int,
    setMode: (Pair<Mode, Int>) -> Unit,
    onExit: () -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.modeTitle),
                    style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                var secs by remember(currentSecs) { mutableIntStateOf(currentSecs) }
                var mode by remember(currentMode) { mutableStateOf(currentMode) }

                Mode.entries.forEach { entry ->
                    ModeRow(
                        entry = entry,
                        isSelected = entry == mode,
                        secs = secs,
                        onSelect = { mode = entry },
                        onValueChange = { secs = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                DialogActions(
                    onAccept = { setMode(mode to secs) },
                    onCancel = onExit
                )
            }
        }
    }
}

@Composable
private fun ModeRow(
    entry: Mode,
    isSelected: Boolean,
    secs: Int,
    onSelect: () -> Unit,
    onValueChange: (Int) -> Unit
) {
    val bgColor      = if (isSelected) AccentColor.copy(alpha = 0.15f) else Color.Transparent
    val contentColor = if (isSelected) AccentColor else TextPrimary
    val mutedColor   = if (isSelected) AccentColor.copy(alpha = 0.7f) else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onSelect() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            Icon(Icons.Rounded.Check, contentDescription = null, tint = AccentColor, modifier = Modifier.size(20.dp))
        } else {
            Spacer(modifier = Modifier.size(20.dp))
        }

        Text(
            text = entry.name,
            style = TextStyle(fontSize = 17.sp),
            color = contentColor,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        )

        if (entry.hasIntValue()) {
            val displayText = if (entry.isGradient())
                stringResource(R.string.grad, secs / 10)
            else
                stringResource(R.string.sec, secs)

            Text(text = displayText, style = TextStyle(fontSize = 16.sp), color = mutedColor,
                modifier = Modifier.padding(end = 4.dp))

            IntPickerButtons(
                tint = mutedColor,
                onIncrement = {
                    val max  = if (entry.isGradient()) 220 else 20
                    val step = if (entry.isGradient()) 10  else 1
                    if (secs < max) { onSelect(); onValueChange(secs + step) }
                },
                onDecrement = {
                    val step = if (entry.isGradient()) 10 else 1
                    if (secs > 0) { onSelect(); onValueChange(secs - step) }
                }
            )
        }
    }
}

@Composable
private fun IntPickerButtons(
    tint: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column {
        Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = null, tint = tint,
            modifier = Modifier.size(22.dp).clickable { onIncrement() })
        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = tint,
            modifier = Modifier.size(22.dp).clickable { onDecrement() })
    }
}

// ── 9 MaintenanceDialog ───────────────────────────────────────────────────────
@Composable
fun MaintenanceDialog(
    show: Boolean,
    id: String,
    upgrading: State,
    lastServer: String,
    lastPort: String,
    upgrade: (Pair<String, String>) -> Unit,
    name: String,
    local: () -> Unit,
    full: () -> Unit,
    onExit: () -> Unit
) {
    var showProgress by remember { mutableStateOf(false) }
    var server by remember { mutableStateOf(lastServer) }
    var port   by remember { mutableStateOf(lastPort)   }
    if (!show) return
    Dialog(onDismissRequest = onExit) {
        DialogSurface {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // ── ID ──
                SectionLabel(stringResource(R.string.idToReceive))
                SectionBox {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("ID: ", style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp))
                        SelectionContainer {
                            Text(id, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // ── Firmware ──
                SectionLabel(stringResource(R.string.firmwareUpgradeTitle))
                SectionBox {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.server), style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary)
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            value = server, singleLine = true, maxLines = 1,
                            onValueChange = { server = it }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.port), style = TextStyle(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 10.dp))
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            value = port, singleLine = true, maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = { port = it }
                        )
                    }
                    when (upgrading) {
                        State.UPGRADE      -> { showProgress = true }
                        State.SERVER_FAIL  -> { showProgress = false
                            Text(stringResource(R.string.badPortServer), style = TextStyle(fontSize = 16.sp),
                                color = Color.Red, modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) }
                        State.UPGRADE_FAIL -> { showProgress = false
                            Text(stringResource(R.string.upgradeFails), style = TextStyle(fontSize = 16.sp),
                                color = Color.Red, modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) }
                        State.UPGRADED     -> { showProgress = false
                            Text(stringResource(R.string.upgradeSuccess), style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) }
                        else -> {}
                    }
                    if (showProgress) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    ElevatedButton(onClick = { upgrade(Pair(server, port)) }) {
                        Icon(Icons.Rounded.Upgrade, contentDescription = null)
                        Text(stringResource(R.string.upgrade))
                    }
                }

                // ── Borrar ──
                SectionLabel(stringResource(R.string.eraseTitle))
                SectionBox {
                    var erasing     by remember { mutableStateOf(false) }
                    var fullErasing by remember { mutableStateOf(false) }

                    ElevatedButton(onClick = { erasing = true }) {
                        Icon(Icons.Rounded.PhonelinkErase, contentDescription = null)
                        Text(stringResource(R.string.localErase, name),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 5.dp))
                    }
                    if (erasing) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.sure), style = TextStyle(fontSize = 14.sp),
                                color = MaterialTheme.colorScheme.primary)
                            Button(onClick = { local() }) { Text(stringResource(R.string.yes)) }
                            Button(onClick = { erasing = false }) { Text(stringResource(R.string.no)) }
                        }
                    }

                    ElevatedButton(onClick = { fullErasing = true }) {
                        Icon(Icons.Rounded.Delete, contentDescription = null)
                        Text(stringResource(R.string.fullErase, name),
                            style = TextStyle(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 5.dp))
                    }
                    if (fullErasing) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.sure), style = TextStyle(fontSize = 14.sp),
                                color = MaterialTheme.colorScheme.primary)
                            Button(onClick = { full() }) { Text(stringResource(R.string.yes)) }
                            Button(onClick = { fullErasing = false }) { Text(stringResource(R.string.no)) }
                        }
                    }
                }

                // ── Cerrar ──
                Row(Modifier.fillMaxWidth(), Arrangement.End) {
                    TextButton(onClick = { showProgress = false; onExit() }) {
                        Icon(Icons.Rounded.Close, contentDescription = null, tint = AccentColor)
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.noAccept))
                    }
                }
            }
        }
    }
}

// Helpers internos para MaintenanceDialog
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 14.sp),
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
private fun SectionBox(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White.copy(alpha = 0.06f))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        content()
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun ShowDialog(value: Int = 8) {
    when (value) {
        1 -> AddSwDialog(true, {}, {}, {}, {})
        2 -> NewDialog(true, ApData("myHome", "", false), TouchState.IN_PROGRESS, { "" }, {})
        3 -> NewIdDialog(show = true, setId = {}, {})
        4 -> NewAllDialog(true, "all", {}, {})
        5 -> NameDialog(true, "kitchen light", { "" }, {})
        6 -> IconDialog(true, "lightbulb", { "theatermasks" }, {})
        7 -> TimerDialog(true, WeeklyProgram(2, 3, 1), {}, {})
        8 -> ModeDialog(true, Mode.TEMP, 10, {}, {})
        9 -> MaintenanceDialog(true, "1234", State.SERVER_FAIL, "", "", {}, "kitchen light", {}, {}, {})
    }
}