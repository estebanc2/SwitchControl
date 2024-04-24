package com.capa1.switchcontrol.ui

import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.MyColors
import com.capa1.switchcontrol.data.model.SwScreenData

@Composable
fun SwListScreen(
    viewModel: SwViewModel = hiltViewModel()
) {
    //val screenModifiers by viewModel.screenModifiers.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.start()
    }
    AddSwDialog(
        show = viewModel.showAdd,
        addSw = {},
        addId = {viewModel.onShowNewId(true)},
        addAll = {},
        onExit = {viewModel.onShowAdd(false)}
    )
    NameDialog(
        show = viewModel.showName,
        currentName = viewModel.configurableData.name ,
        setName = {name -> viewModel.changeName(name)},
        onExit = {viewModel.onShowName(false)}
    )
    NewIdDialog(
        show = viewModel.showNewId,
        setId = {id -> viewModel.addSwId(id)},
        onExit = {viewModel.onShowNewId(false)
                    viewModel.onShowAdd(false)}
    )
    ColorDialog(
        show = viewModel.showColor,
        currentColor = viewModel.configurableData.bkColor,
        setColor = {color -> viewModel.changeColor(color)},
        exit = {viewModel.onShowColor(false)}
    )
    TimerDialog(
        show = viewModel.showTimer,
        currentTimer = viewModel.configurableData.prgs[0],
        setTimer = {timer -> viewModel.changeTimer(timer)},
        onExit = {viewModel.onShowTimer(false)}
    )
    ModeDialog(
        show = viewModel.showName,
        onExit = {viewModel.onShowName(false)}
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
            ShowConfig(
                data = viewModel.configurableData,
                showPicker = {viewModel.onShowColor(true)},
                save = {viewModel.saveConfig()},
                exit = {viewModel.exitConfig()}
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
                    text = item.timerInfo
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