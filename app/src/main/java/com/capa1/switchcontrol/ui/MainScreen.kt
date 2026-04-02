package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.ui.permissions.PermissionUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// ── Design tokens (matching the SwiftUI app) ────────────────────────────────
private val BgPage        = Color(0xFF1C1C1E)   // main background
private val BgCard        = Color(0xFF2C2C2E)   // inactive card
private val BgCardActive  = Color(0xFF1A2E24)   // active card tint
private val BgIcon        = Color(0xFF3A3A3C)   // icon container (inactive)
private val BgIconActive  = Color(0xFF1F4D35)   // icon container (active)
private val AccentGreen   = Color(0xFF34C759)   // iOS system green
private val TextPrimary   = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF8E8E93)
private val DividerColor  = Color(0xFF3A3A3C)
private val BorderActive  = Color(0xFF34C759)
private val BorderInactive= Color(0xFF3A3A3C)

// ── Main entry point ─────────────────────────────────────────────────────────
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: SwViewModel = hiltViewModel()
) {
    Log.i(TAG, "init MainScreen")
    val permissionState =
        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions).allPermissionsGranted
    LaunchedEffect(key1 = permissionState) {
        if (permissionState) viewModel.start()
    }
    val activity = LocalActivity.current

    // All dialogs unchanged
    NoPermissionDialog(show = !permissionState, onConfirm = { activity?.finish() })
    AddSwDialog(
        show = viewModel.showAdd && permissionState,
        addSw = { viewModel.onShowNew(true) },
        addId = { viewModel.onShowNewId(true) },
        addAll = { viewModel.onShowAll(true) },
        onExit = { viewModel.onShowAdd(false) }
    )
    NewDialog(
        show = viewModel.showNew,
        apData = viewModel.myAp,
        state = viewModel.touchProgress,
        setPass = { pass -> viewModel.discoverSwitches(pass) },
        onExit = { viewModel.onShowNew(false) }
    )
    NewIdDialog(
        show = viewModel.showNewId,
        setId = { id -> viewModel.setSwWithId(id) },
        onExit = { viewModel.onShowNewId(false) }
    )
    NewAllDialog(
        show = viewModel.showAll,
        allSwId = viewModel.allSwId,
        setId = { id -> viewModel.addAllSw(id) },
        onExit = { viewModel.onShowAll(false) }
    )
    NameDialog(
        show = viewModel.showName,
        currentName = viewModel.currentSwData.name,
        setName = { name -> viewModel.newName(name) },
        onExit = { viewModel.onShowName(false) }
    )
    ColorDialog(
        show = viewModel.showColor,
        currentColor = viewModel.currentSwData.icon,
        setColor = { color -> viewModel.newColor(color) },
        exit = { viewModel.onShowColor(false) }
    )
    TimerDialog(
        show = viewModel.showTimer,
        currentWP = viewModel.currentSwData.prgs[viewModel.currentTimer],
        setTimer = { timer -> viewModel.newTimer(timer) },
        onExit = { viewModel.onShowTimer(0, false) }
    )
    ModeDialog(
        show = viewModel.showMode,
        currentMode = viewModel.currentSwData.mode,
        currentSecs = viewModel.currentSwData.secs,
        setMode = { pair -> viewModel.setMode(pair.first, pair.second) },
        onExit = { viewModel.onShowMode(false) }
    )
    MaintenanceDialog(
        show = viewModel.showMaintenance,
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

    Box(
        Modifier
            .fillMaxSize()
            .background(BgPage)
    ) {
        if (!viewModel.showConfig) {
            Column {
                ShowTitle(onShowAdd = { viewModel.onShowAdd(true) })
                ShowSwitches(
                    switches = viewModel.swScreenList,
                    click = { id -> viewModel.imageClick(id) },
                    onConfig = { item -> viewModel.goConfig(item) }
                )
            }
        } else {
            ConfigScreen(
                qty = viewModel.swScreenList.size,
                data = viewModel.currentSwData,
                changeName = { viewModel.onShowName(true) },
                changeColor = { viewModel.onShowColor(true) },
                changeRow = { pos -> viewModel.changeRow(pos) },
                changeTimer = { timer -> viewModel.onShowTimer(timer, true) },
                changeMode = { viewModel.onShowMode(true) },
                goMaintenance = { viewModel.onShowMaintenance(true) },
                save = { viewModel.saveConfig() },
                onExit = { viewModel.exitConfig() }
            )
        }
    }
}

// ── Title bar ────────────────────────────────────────────────────────────────
@Composable
fun ShowTitle(onShowAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.mainScreenTitle),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(BgCard)
                .clickable { onShowAdd() }
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Agregar",
                tint = AccentGreen,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ── Switch list ──────────────────────────────────────────────────────────────
@Composable
fun ShowSwitches(
    switches: List<SwScreenData>,
    click: (String) -> Unit,
    onConfig: (SwScreenData) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(switches) { switch ->
            SwRow(
                item = switch,
                click = { click(switch.id) },
                onConfig = { item -> onConfig(item) }
            )
            // thin divider between cards (like iOS list separators)
            Spacer(modifier = Modifier.height(0.dp)) // spacing handled above
        }
    }
}

// ── Individual switch card ────────────────────────────────────────────────────
@Composable
fun SwRow(
    item: SwScreenData,
    click: () -> Unit,
    onConfig: (SwScreenData) -> Unit
) {
    val isOn = item.isOn

    // Animated card background and border
    val cardBg by animateColorAsState(
        targetValue = if (isOn) BgCardActive else BgCard,
        animationSpec = tween(300), label = "cardBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isOn) BorderActive else BorderInactive,
        animationSpec = tween(300), label = "border"
    )
    val accentBarColor by animateColorAsState(
        targetValue = if (isOn) AccentGreen else Color(0xFF48484A),
        animationSpec = tween(300), label = "accentBar"
    )
    val iconBg by animateColorAsState(
        targetValue = if (isOn) BgIconActive else BgIcon,
        animationSpec = tween(300), label = "iconBg"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isOn) AccentGreen else TextSecondary,
        animationSpec = tween(300), label = "iconTint"
    )
    val nameColor by animateColorAsState(
        targetValue = if (isOn) TextPrimary else Color(0xFFAEAEB2),
        animationSpec = tween(300), label = "nameColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(
                width = if (isOn) 1.dp else 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left accent bar (like iOS)
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                    .background(accentBarColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Icon box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "",
                )
                /* Use the existing icon drawable from the item
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.name,
                    tint = iconTint,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onConfig(item) }
                )*/
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name + status info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onConfig(item) },
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    fontSize = 17.sp,
                    fontWeight = if (isOn) FontWeight.SemiBold else FontWeight.Normal,
                    color = nameColor,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.timerInfo,
                    fontSize = 13.sp,
                    color = if (isOn) AccentGreen.copy(alpha = 0.85f) else TextSecondary,
                    maxLines = 2,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Toggle — Material3 Switch styled to match iOS green
            Switch(
                checked = isOn,
                onCheckedChange = { click() },
                modifier = Modifier.padding(end = 14.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor     = Color.White,
                    checkedTrackColor     = AccentGreen,
                    checkedBorderColor    = AccentGreen,
                    uncheckedThumbColor   = Color(0xFF8E8E93),
                    uncheckedTrackColor   = Color(0xFF3A3A3C),
                    uncheckedBorderColor  = Color(0xFF48484A)
                )
            )
        }
    }
}