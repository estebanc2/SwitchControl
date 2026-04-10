package com.capa1.switchcontrol.ui

//import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.IconMapper
import com.capa1.switchcontrol.data.model.ScreenData
import com.capa1.switchcontrol.ui.permissions.PermissionUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.capa1.switchcontrol.ui.theme.*

// ── Main entry point ─────────────────────────────────────────────────────────
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: SwViewModel = hiltViewModel())
{
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState =
        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions).allPermissionsGranted
    LaunchedEffect(key1 = permissionState) {
        if (permissionState)  {
            viewModel.start()
        } else {
            Log.i(TAG, " - - - - -sin permisos!! - - - - - - ")
        }
    }
    val activity = LocalActivity.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> viewModel.saveData()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    // All dialogs unchanged
    NoPermissionDialog(show = !permissionState, onConfirm = { activity?.finish() })
    AddSwDialog(
        show = viewModel.dialogState.showAdd && permissionState,
        addSw = { viewModel.onShowNew(true) },
        addId = { viewModel.onShowNewId(true) },
        addAll = { viewModel.onShowAll(true) },
        onExit = { viewModel.onShowAdd(false) }
    )
    NewDialog(
        show = viewModel.dialogState.showNew,
        apData = viewModel.myAp,
        state = viewModel.touchProgress,
        setPass = { pass -> viewModel.discoverSwitches(pass) },
        onExit = { viewModel.onShowNew(false) }
    )
    NewIdDialog(
        show = viewModel.dialogState.showNewId,
        setId = { id -> viewModel.setSwWithId(id) },
        onExit = { viewModel.onShowNewId(false) }
    )
    NewAllDialog(
        show = viewModel.dialogState.showAll,
        allSwId = viewModel.allSwId,
        setId = { id -> viewModel.addAllSw(id) },
        onExit = { viewModel.onShowAll(false) }
    )
    NameDialog(
        show = viewModel.dialogState.showName,
        currentName = viewModel.currentSwData.name,
        setName = { name -> viewModel.newName(name) },
        onExit = { viewModel.onShowName(false) }
    )
    IconDialog(
        show = viewModel.dialogState.showIcon,
        currentIcon = viewModel.currentSwData.icon,
        setIcon = { icon -> viewModel.newIcon(icon) },
        onExit = { viewModel.onShowIcon(show = false) }
    )
    TimerDialog(
        show = viewModel.dialogState.showTimer,
        currentWP = viewModel.currentSwData.prgs[viewModel.currentTimer],
        setTimer = { timer -> viewModel.newTimer(timer) },
        onExit = { viewModel.onShowTimer(0, false) }
    )
    ModeDialog(
        show = viewModel.dialogState.showMode,
        currentMode = viewModel.currentSwData.mode,
        currentSecs = viewModel.currentSwData.secs,
        setMode = { pair -> viewModel.setMode(pair.first, pair.second) },
        onExit = { viewModel.onShowMode(false) }
    )
    MaintenanceDialog(
        show = viewModel.dialogState.showMaintenance,
        id = viewModel.currentId,
        upgrading = viewModel.upgrading,
        lastServer = viewModel.server,
        lastPort = viewModel.port,
        upgrade = { pair -> viewModel.firmwareUpgrade(pair.first, pair.second) },
        name = viewModel.currentSwData.name,
        local = { viewModel.localErase() },
        full = { viewModel.fullErase() },
        onExit = { viewModel.onShowMaintenance(false) }
    )
    if (!viewModel.dialogState.showConfig) {
        Column {
            ShowTitle(onShowAdd = { viewModel.onShowAdd(true) })
            ShowSwitches(
                switches = viewModel.swScreenList,
                click = { id -> viewModel.toggle(id) },
                onConfig = { item -> viewModel.goConfig(item) }
            )
        }
    } else {
        ConfigScreen(
            qty = viewModel.swScreenList.size,
            data = viewModel.currentSwData,
            changeName = { viewModel.onShowName(true) },
            changeIcon = { viewModel.onShowIcon(true) },
            changeRow = { pos -> viewModel.changeRow(pos) },
            changeTimer = { timer -> viewModel.onShowTimer(timer, true) },
            changeMode = { viewModel.onShowMode(true) },
            goMaintenance = { viewModel.onShowMaintenance(true) },
            save = { viewModel.saveConfig() },
            onExit = { viewModel.exitConfig() }
        )
    }
}

// ── Title bar ────────────────────────────────────────────────────────────────
@Composable
fun ShowTitle(onShowAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .background(BgCard)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.mainScreenTitle),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
        )
        IconButton (  onClick = { onShowAdd() } )
        {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Agregar",
                tint = AccentColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ── Switch list ──────────────────────────────────────────────────────────────
@Composable
fun ShowSwitches(
    switches: List<ScreenData>,
    click: (String) -> Unit,
    onConfig: (ScreenData) -> Unit
) {
   LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
   ) {
        items(switches) { switch ->
            RowView(
                screenData = switch,
                onToggle = { click(switch.id) },
                onConfig = { item -> onConfig(item) }
            )
        }
    }
}

@Composable
fun RowView(
    screenData: ScreenData,
    onToggle: (String) -> Unit,
    onConfig: (ScreenData) -> Unit //onConfigTap: (String) -> Unit,
) {
    val isOn = screenData.swOn
    val connected = screenData.connected

    // Animaciones de color
    val accentBarColor by animateColorAsState(
        targetValue = if (isOn) AccentColor else Color.White.copy(alpha = 0.1f),
        animationSpec = tween(300), label = "barColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isOn) AccentColor.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.06f),
        animationSpec = tween(300), label = "borderColor"
    )
    val bgAlpha by animateFloatAsState(
        targetValue = if (isOn) 0.06f else 0.03f,
        animationSpec = tween(300), label = "bgAlpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(
                color = Color.White.copy(alpha = bgAlpha),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Barra izquierda de acento
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(48.dp)
                .background(
                    color = accentBarColor,
                    shape = RoundedCornerShape(2.dp)
                )
        )

        // Ícono
        val iconBgColor by animateColorAsState(
            targetValue = if (isOn) AccentColor.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.06f),
            animationSpec = tween(300), label = "iconBg"
        )
        val iconTint by animateColorAsState(
            targetValue = if (isOn) AccentColor else Color.Gray,
            animationSpec = tween(1000), label = "iconTint"
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = iconBgColor, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IconMapper.fromName(screenData.icon),
                contentDescription = screenData.name,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        // Texto
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onConfig(screenData) },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = screenData.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (connected) Color.White else Color.Gray.copy(alpha = 0.7f)
            )
            Text(
                text = screenData.timerInfo,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = if (isOn) AccentColor.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.7f),
                maxLines = 3
            )
        }

        // Toggle
        ToggleSwitch(
            isOn = isOn,
            isConnected = connected,
            onToggle = { onToggle(screenData.id) }
        )
    }
}


@Composable
fun ToggleSwitch(
    isOn: Boolean,
    isConnected: Boolean,
    onToggle: () -> Unit,
) {
    val trackColor by animateColorAsState(
        targetValue = when {
            !isConnected -> Color.Gray.copy(alpha = 0.25f)
            isOn         -> AccentColor
            else         -> Color.White.copy(alpha = 0.12f)
        },
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "trackColor"
    )

    // Posición del thumb: 0f = izquierda, 1f = derecha
    val thumbOffset by animateFloatAsState(
        targetValue = if (isOn) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "thumbOffset"
    )

    val trackWidth = 48.dp
    val trackHeight = 26.dp
    val thumbSize = 20.dp
    val thumbPadding = 3.dp

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(trackHeight)
            .alpha(if (isConnected) 1f else 0.6f)
            .background(color = trackColor, shape = CircleShape)
            .clickable(enabled = isConnected) { onToggle() },
    ) {
        // Thumb
        Box(
            modifier = Modifier
                .padding(thumbPadding)
                .size(thumbSize)
                .align(Alignment.CenterStart)
                .offset(
                    x = (thumbOffset * (trackWidth - thumbSize - thumbPadding * 2).value).dp
                )
                .shadow(elevation = 2.dp, shape = CircleShape)
                .background(Color.White, CircleShape)
        )
    }
}
