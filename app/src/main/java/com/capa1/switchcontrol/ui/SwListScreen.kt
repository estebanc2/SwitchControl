package com.capa1.switchcontrol.ui

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
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
    Box(Modifier.fillMaxSize()) {
        if (!screenModifiers.showList) {
            ShowSwitches(
                viewModel,
                list = screenModifiers.swList
            )
        }
    }
}

@Composable
fun ShowSwitches(
    //viewModel: ViewModel,
    list: List<SwData>,
    screenModifiers: ScreenModifiers
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
        items(list) { calValue ->
            SwRow(
                item = calValue,
                swScreenData = screenModifiers.swScreenMap [calValue.topic] ?: SwScreenData(
                    SwImages.NO_INFO,
                    ""
                ),
                config = viewModel.
            )
                //swImage = {topic -> viewModel.swImage(topic)})
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
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween){
            Column(){
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
            var painter: Painter
            when(swScreenData.swImage){
                SwImages.OPEN -> painter = painterResource(id = R.mipmap.open)
                SwImages.CLOSE -> painter = painterResource(id = R.mipmap.close)
                SwImages.OPENING -> painter = painterResource(id = R.mipmap.opening)
                SwImages.CLOSING -> painter = painterResource(id = R.mipmap.closing)
                SwImages.CLOSE_LOCK -> painter = painterResource(id = R.mipmap.close_lock)
                SwImages.OPEN_LOCK -> painter = painterResource(id = R.mipmap.open_lock)
                SwImages.NC -> painter = painterResource(id = R.mipmap.nc)
                SwImages.NA -> painter = painterResource(id = R.mipmap.na)
                SwImages.NO_INFO -> painter = painterResource(id = R.mipmap.no_info)
            }
            Image(
                painter,
                contentDescription = "",
                Modifier
                    .clickable { }
                    .size(70.dp, 70.dp)
            )
        }
    }
}
