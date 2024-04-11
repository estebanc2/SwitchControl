package com.capa1.switchcontrol.ui

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.ui.navigation.AppScreens


@Composable
fun SwRow(
    navController: NavController,
    item: SwData,
    leyend: String,
    //swImage: (String) -> Unit
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
                    modifier = Modifier.clickable { navController.navigate(route = AppScreens.ConfigScreen.route) }
                    //color = MaterialTheme.colorScheme.primary
                )
                Text (
                    text = leyend
                )
            }
            //Image(swImage(item.topic)), //id = R.mipmap.open_foreground),
              //  contentDescription = "",
                //Modifier
                  //  .clickable { }
                    //.size(70.dp, 70.dp)
            //)
        }
    }
}


