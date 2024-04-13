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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwImages
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.SwStatus

@Composable
fun SwListScreen(
    viewModel: SwViewModel = hiltViewModel()
) {
    val screenModifiers by viewModel.screenModifiers.collectAsState()
    ConfigDialog (
        show = screenModifiers.showConfig,
        setName = {name -> viewModel.changeName(name)},
        onExit = {viewModel.showConfig(false)}
    )

    Box(Modifier.fillMaxSize()) {
        if (!screenModifiers.showList) {
            ShowSwitches(
                switches = screenModifiers.swList,
                swScreenMap = screenModifiers.swScreenMap,
                config = {viewModel.showConfig(true)},
                click = {viewModel.imageClick()}
            )
        }
    }
}

@Composable
fun ShowSwitches(
    switches:List<SwData>,
    swScreenMap: Map<String, SwScreenData>,
    config: () -> Unit,
    click: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(
                    text = "Interruptores WiFi",
                    style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                    modifier = Modifier.clickable {  }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(switches) { calValue ->
            SwRow(
                item = calValue,
                swScreenData = swScreenMap [calValue.topic] ?: SwScreenData(
                    swImage = SwImages.NO_INFO,
                    timerInfo = "Sin información"),
                config = config,
                click = click
            )
        }
    }
}

@Composable
fun SwRow(
    item: SwData,
    swScreenData: SwScreenData,
    config: () -> Unit,
    click: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(item.bkColor)),
            Arrangement.SpaceBetween,
            ){
            Column {
                Text(
                    text = item.name,
                    style = TextStyle(fontSize = 24.sp),
                    modifier = Modifier.clickable{ config() }
                    //color = MaterialTheme.colorScheme.primary
                )
                Text (
                    text = swScreenData.timerInfo
                )
            }
            val painter: Painter = when(swScreenData.swImage){
                SwImages.OPEN -> painterResource(id = R.drawable.ic_louncher_background)
                SwImages.CLOSE -> painterResource(id = R.mipmap.close)
                SwImages.OPENING -> painterResource(id = R.mipmap.opening)
                SwImages.CLOSING -> painterResource(id = R.mipmap.closing)
                SwImages.CLOSE_LOCK -> painterResource(id = R.mipmap.close_lock)
                SwImages.OPEN_LOCK -> painterResource(id = R.mipmap.open_lock)
                SwImages.NC -> painterResource(id = R.mipmap.nc)
                SwImages.NA -> painterResource(id = R.mipmap.na)
                SwImages.NO_INFO -> painterResource(id = R.mipmap.no_info)
            }
            Image(
                painter,
                contentDescription = "",
                Modifier
                    .clickable { click() }
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
fun ScreenPreview() {
    SwRow(item = SwData("luz cocina", "100AA56F", 1, 0xEFB8C8, SwStatus.CONNECTING),
        swScreenData = SwScreenData(SwImages.CLOSE, "frafrafra"),
        config = {},
        click = {})
}