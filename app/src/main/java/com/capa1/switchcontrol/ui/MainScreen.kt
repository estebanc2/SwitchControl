package com.capa1.switchcontrol.ui

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.MyColors
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.ui.permissions.PermissionUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: SwViewModel = hiltViewModel()
) {
    Log.i(TAG, "init MainScreen")
    val permissionState =
        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions).allPermissionsGranted
    LaunchedEffect(key1 = permissionState) {
        if (permissionState) {
            viewModel.start()
        }
    }
    val activity = LocalActivity.current
    NoPermissionDialog(
        show = !permissionState,
        onConfirm = { activity?.finish() }
    )
    AddSwDialog(
        show = viewModel.showAdd && permissionState,
        addSw = {viewModel.onShowNew(true)},
        addId = {viewModel.onShowNewId(true)},
        addAll = {viewModel.onShowAll(true)},
        onExit = {viewModel.onShowAdd(false)}
    )
    NewDialog(
        show = viewModel.showNew,
        apData = viewModel.myAp,
        state = viewModel.touchProgress,
        setPass = {pass -> viewModel.discoverSwitches(pass)},
        onExit = {viewModel.onShowNew(false)}
    )
    NewIdDialog(
        show = viewModel.showNewId,
        setId = {id -> viewModel.setSwWithId(id)},
        onExit = {viewModel.onShowNewId(false)}
    )
    NewAllDialog(
        show = viewModel.showAll,
        allSwId = viewModel.allSwId,
        setId = {id -> viewModel.addAllSw(id)},
        onExit = { viewModel.onShowAll(false)}
    )
    NameDialog(
        show = viewModel.showName,
        currentName = viewModel.currentSwData.name ,
        setName = {name -> viewModel.newName(name)},
        onExit = {viewModel.onShowName(false)}
    )
    ColorDialog(
        show = viewModel.showColor,
        currentColor = viewModel.currentSwData.bkColor,
        setColor = {color -> viewModel.newColor(color)},
        exit = {viewModel.onShowColor(false)}
    )
    TimerDialog(
        show = viewModel.showTimer,
        currentWP = viewModel.currentSwData.prgs[viewModel.currentTimer],
        setTimer = {timer -> viewModel.newTimer(timer)},
        onExit = {viewModel.onShowTimer(0,false)}
    )
    ModeDialog(
        show = viewModel.showMode,
        currentMode = viewModel.currentSwData.mode,
        currentSecs = viewModel.currentSwData.secs,
        setMode = {pair -> viewModel.setMode(pair.first, pair.second)},
        onExit = {viewModel.onShowMode(false)}
    )
    MaintenanceDialog(
        show = viewModel.showMaintenance,
        id = viewModel.currentId,
        upgrading = viewModel.upgrading,
        lastServer = viewModel.server,
        lastPort = viewModel.port,
        upgrade = {pair -> viewModel.firmwareUpgrade(pair.first, pair.second)},
        name = viewModel.currentSwData.name,
        local = {viewModel.localErase()},
        full = {viewModel.fullErase()},
        onExit = {viewModel.onShowMaintenance(false)}
    )

    Box(
        Modifier.fillMaxSize()
    ) {
        if (!viewModel.showConfig){
            Column{
                ShowTitle (
                    onShowAdd = {viewModel.onShowAdd(true)}
                )
                ShowSwitches(
                    switches = viewModel.swScreenList,
                    click = {id -> viewModel.imageClick(id)},
                    onConfig = {item -> viewModel.goConfig(item)}
                )
            }
       }else{
            ConfigScreen (
                qty = viewModel.swScreenList.size,
                data = viewModel.currentSwData,
                changeName = {viewModel.onShowName(true)},
                changeColor = {viewModel.onShowColor(true)},
                changeRow = { pos -> viewModel.changeRow(pos)},
                changeTimer = {timer -> viewModel.onShowTimer(timer, true)},
                changeMode ={viewModel.onShowMode(true)},
                goMaintenance = { viewModel.onShowMaintenance(true)},
                save = {viewModel.saveConfig()},
                onExit = {viewModel.exitConfig()}
            )
        }
    }
}

@Composable
fun ShowTitle(
    onShowAdd: () -> Unit
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.mainScreenTitle),
            style = TextStyle(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            Icons.Default.Add,
            contentDescription = "",
            modifier = Modifier.clickable { onShowAdd() }
        )
    }
}
@Composable
fun ShowSwitches(
    switches:List<SwScreenData>,
    click: (String) -> Unit,
    onConfig: (SwScreenData)-> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(switches) { switch ->
            SwRow(
                item = switch,
                click = { click(switch.id) },
                onConfig = {item -> onConfig(item)}
            )
        }
    }
}

@Composable
fun SwRow(
    item: SwScreenData,
    click: () -> Unit,
    onConfig: (SwScreenData) -> Unit
){
    val isPortTrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val imageSize = LocalConfiguration.current.screenWidthDp.dp / if (isPortTrait)  6 else 12
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(MyColors[item.bkColor]!!.backColor)
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ){
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
        ){
            Column(modifier = Modifier.width(width = imageSize * 3.5f)) {//
                Text(
                    text = item.name,
                    color = MyColors[item.bkColor]!!.textColor,
                    style = TextStyle(fontSize = 24.sp),
                    modifier = Modifier.clickable{ onConfig(item) }
                )
                Text (
                    text = item.timerInfo,
                    color = MyColors[item.bkColor]!!.textColor,
                )
            }
            Image(
                painterResource(id = item.swImageId),
                contentDescription = "",
                Modifier
                    .clickable { click() }
                    .size(imageSize, imageSize)
            )
        }
    }
}
