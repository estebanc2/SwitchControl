package com.capa1.switchcontrol.ui.screens

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
import androidx.navigation.NavController
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwImages
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.ui.SwViewModel
import com.capa1.switchcontrol.ui.navigation.AppScreens

@Composable
fun SwListScreen(
    navController: NavController,
    viewModel: SwViewModel = hiltViewModel()
) {
    val screenModifiers by viewModel.screenModifiers.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.start()
    }

    Box(Modifier.fillMaxSize()) {
        ShowSwitches(
            navController = navController,
            switches = screenModifiers.swList,
            swScreenMap = screenModifiers.swScreenMap,
            click = {id -> viewModel.imageClick(id)}
        )
    }
}

@Composable
fun ShowSwitches(
    navController: NavController,
    switches:List<SwData>,
    swScreenMap: Map<String, SwScreenData>,
    click: (String) -> Unit
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
                    modifier = Modifier.clickable { navController.navigate(route = AppScreens.MaintenanceScreen.route) }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(switches) { calValue ->
            SwRow(
                navController = navController,
                item = calValue,
                swScreenData = swScreenMap [calValue.id] ?: SwScreenData(
                    swImage = SwImages.NO_INFO,
                    timerInfo = "Sin información"),
                click = { click(calValue.id) }
            )
        }
    }
}

@Composable
fun SwRow(
    navController: NavController,
    item: SwData,
    swScreenData: SwScreenData,
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
                    modifier = Modifier.clickable{ navController.navigate(route = AppScreens.ConfigScreen.route) }
                    //color = MaterialTheme.colorScheme.primary
                )
                Text (
                    text = swScreenData.timerInfo
                )
            }
            val painter: Painter = when(swScreenData.swImage){
                SwImages.OPEN -> painterResource(id = R.drawable.open)
                SwImages.CLOSE -> painterResource(id = R.drawable.close)
                SwImages.OPENING -> painterResource(id = R.drawable.opening)
                SwImages.CLOSING -> painterResource(id = R.drawable.closing)
                SwImages.CLOSE_LOCK -> painterResource(id = R.drawable.close_lock)
                SwImages.OPEN_LOCK -> painterResource(id = R.drawable.open_lock)
                SwImages.NC -> painterResource(id = R.drawable.nc)
                SwImages.NA -> painterResource(id = R.drawable.na)
                SwImages.NO_INFO -> painterResource(id = R.drawable.no_info)
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
fun ScreenPreview(value: Int = 2) {
    val navController = null
    when (value) {
        1 -> navController?.let {
            SwRow(
                navController = it,
                item = SwData("luz cocina", "100AA56F", 1, 0xEFB8C8, SwStatus.CONNECTING),
                swScreenData = SwScreenData(SwImages.CLOSE, "frafrafra"),
                click = {})
        }
        2 -> navController?.let {
            ShowSwitches(
                navController = it,
                switches = listOf(
                    SwData("velador", "00AB", 1, 1, SwStatus.DISCONNECTED),
                    SwData("luz cocina", "10AB", 2, 0x00FF00, SwStatus.DISCONNECTED),
                    SwData("riego", "20AB", 3, 1, SwStatus.DISCONNECTED),
                    SwData("TV", "30AB", 4, 1, SwStatus.DISCONNECTED)
                ),
                swScreenMap = mapOf (
                    "00AB" to SwScreenData(SwImages.CLOSE, "frafrafra"),
                    "10AB" to SwScreenData(SwImages.OPENING, "todo liso"),
                    "20AB" to SwScreenData(SwImages.NC, "faltan 16h"),
                    "30AB" to SwScreenData(SwImages.CLOSE_LOCK, "cuando quiera"),
                ) ,
                click = {})
        }
    }
}