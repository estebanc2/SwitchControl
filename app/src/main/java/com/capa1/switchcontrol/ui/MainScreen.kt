package com.capa1.switchcontrol.ui

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
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
    val permissionState =
        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner){
        permissionState.launchMultiplePermissionRequest()
        onDispose { Log.i(TAG,"SE CIERRA LA APP??") }
    }
    LaunchedEffect(key1 = permissionState.allPermissionsGranted) {
        viewModel.start()
    }
    val activity = (LocalContext.current as? Activity)
    NoPermissionDialog(show = !permissionState.allPermissionsGranted,
        onConfirm = { activity?.finish() }
    )
    AddSwDialog(
        show = viewModel.showAdd,
        addSw = {viewModel.onShowNew(true)},
        addId = {viewModel.onShowNewId(true)},
        addAll = {viewModel.onShowAll(true)},
        onExit = {viewModel.onShowAdd(false)}
    )
    NewDialog(
        show = viewModel.showNew,
        apData = viewModel.myAp,
        state = viewModel.touchProgress,
        setPass = {pass -> viewModel.setPass(pass)},
        onExit = {viewModel.onShowNew(false)}
    )
    NewIdDialog(
        show = viewModel.showNewId,
        setId = {id -> viewModel.addSwId(id)},
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
        currentName = viewModel.configurableData.name ,
        setName = {name -> viewModel.newName(name)},
        onExit = {viewModel.onShowName(false)}
    )
    ColorDialog(
        show = viewModel.showColor,
        currentColor = viewModel.configurableData.bkColor,
        setColor = {color -> viewModel.newColor(color)},
        exit = {viewModel.onShowColor(false)}
    )
    TimerDialog(
        show = viewModel.showTimer,
        currentWP = viewModel.configurableData.prgs[viewModel.currentTimer],
        setTimer = {timer -> viewModel.newTimer(timer)},
        onExit = {viewModel.onShowTimer(0,false)}
    )
    ModeDialog(
        show = viewModel.showMode,
        currentMode = viewModel.configurableData.mode,
        currentSecs = viewModel.configurableData.secs,
        setMode = {pair -> viewModel.setMode(pair.first, pair.second)},
        onExit = {viewModel.onShowMode(false)}
    )
    MaintenanceDialog(
        show = viewModel.showMaintenance,
        id = viewModel.id,
        upgrade = {pair -> viewModel.upgrade(pair.first, pair.second)},
        name = viewModel.configurableData.name,
        local = {viewModel.localErase()},
        full = {viewModel.fullErase()},
        onExit = {viewModel.onShowMaintenance(false)}
    )
    Box(Modifier.fillMaxSize()) {
        if (!viewModel.goConfig){
            ShowSwitches(
                switches = viewModel.swScreenList,
                onShowAdd = {viewModel.onShowAdd(true)},
                click = {id -> viewModel.imageClick(id)},
                onConfig = {item -> viewModel.onConfig(true, item)}
            )
        }else{
            ConfigScreen (
                qty = viewModel.swScreenList.size,
                swState = viewModel.swState,
                data = viewModel.configurableData,
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
fun ShowSwitches(
    switches:List<SwScreenData>,
    onShowAdd: () -> Unit,
    click: (String) -> Unit,
    onConfig: (SwScreenData)-> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(
                    text = "Interruptores WiFi",
                    style = TextStyle(fontSize = 30.sp),
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier.clickable { onShowAdd() }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
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
    var image = item.swImageId
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(MyColors[item.bkColor]!!.backColor)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ){
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            ){
            Column {
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
            painterResource(id = image),
            contentDescription = "",
            Modifier
                .clickable {
                    click()
                    when (image) {
                        R.drawable.close -> image = R.drawable.opening
                        R.drawable.open -> image = R.drawable.closing
                    }
                }
                .size(70.dp, 70.dp)
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun ScreenPreview(value: Int = 2) {
    when (value) {
        1 -> SwRow(
                item = SwScreenData("luz cocina", "100AA56F", 1,"limon",  R.drawable.close_lock, "Cambia en mas de 24h"),
                click = {}, {}
        )
        2 -> ShowSwitches(
                switches = listOf(
                    SwScreenData("velador", "483fda877368", 2, "limon", R.drawable.close, "sin informacion"),
                    SwScreenData("luz cocina", "98f4abb33d5a", 1, "lila", R.drawable.close, "sin informacion"),
                    SwScreenData("riego", "483fda878e46", 3, "pasto", R.drawable.close, "sin informacion"),
                    SwScreenData("alargue", "bcddc247dbc9", 5, "palta", R.drawable.close, "sin informacion"),
                    SwScreenData("TV", "483fda879484", 4, "madera", R.drawable.close, "sin informacion")
                ), {}, {}, {}
        )
    }
}