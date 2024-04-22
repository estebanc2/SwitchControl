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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.MyColors
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.ui.AddSwDialog
import com.capa1.switchcontrol.ui.SwViewModel

@Composable
fun SwListScreen(
    navController: NavController,
    viewModel: SwViewModel = hiltViewModel()
) {
    //val screenModifiers by viewModel.screenModifiers.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.start()
    }
    AddSwDialog(
        show = viewModel.showAdd,
        navController = navController,
        exit = {viewModel.onShowAdd(false)}
    )
    Box(Modifier.fillMaxSize()) {
        ShowSwitches(
            navController = navController,
            switches = viewModel.swScreenList,
            onShowAdd = {viewModel.onShowAdd(true)},
            click = {id -> viewModel.imageClick(id)}
        )
    }
}

@Composable
fun ShowSwitches(
    navController: NavController,
    switches:List<SwScreenData>,
    onShowAdd: () -> Unit,
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
                    modifier = Modifier.clickable { onShowAdd() }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(switches) { switch ->
            SwRow(
                navController = navController,
                item = switch,
                click = { click(switch.id) }
            )
        }
    }
}

@Composable
fun SwRow(
    navController: NavController,
    item: SwScreenData,
    click: () -> Unit
){
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

                    modifier = Modifier.clickable{ navController.navigate("Config/${item.id}") }
                    //color = MaterialTheme.colorScheme.primary
                )
                Text (
                    text = item.timerInfo
                )
            }
        Image(
                painterResource(id = item.swImageId),
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
    when (value) {
        1 -> SwRow(
                navController = rememberNavController(),
                item = SwScreenData("luz cocina", "100AA56F", "limon", 1, R.drawable.close_lock, "Cambia en mas de 24h"),
                click = {})
        2 -> ShowSwitches(
                navController = rememberNavController(),
                switches = listOf(
                    SwScreenData("velador", "00AB",  "nada",1, R.drawable.close, "sin informacion"),
                    SwScreenData("luz cocina", "10AB", "metal", 2, R.drawable.close, "sin informacion"),
                    SwScreenData("riego", "20AB",  "madera",3, R.drawable.close, "sin informacion"),
                    SwScreenData("TV", "30AB",  "mar",4, R.drawable.close, "sin informacion")
                ),
            {},
                click = {})
    }
}