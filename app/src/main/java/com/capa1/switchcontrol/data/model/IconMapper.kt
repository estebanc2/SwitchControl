package com.capa1.switchcontrol.data.model

// IconMapper.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.ElectricalServices
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Light
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.TheaterComedy
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WindPower
import androidx.compose.ui.graphics.vector.ImageVector

object IconMapper {
    fun fromName(name: String): ImageVector = when (name) {
        "wind"                      -> Icons.Rounded.Air
        "exclamationmark.triangle"  -> Icons.Rounded.WarningAmber
        "cup.and.saucer"            -> Icons.Rounded.Coffee
        "speedometer"               -> Icons.Rounded.Speed
        "theatermasks"              -> Icons.Rounded.TheaterComedy
        "scope"                     -> Icons.Rounded.FilterAlt
        "drop"                      -> Icons.Rounded.WaterDrop
        "lightbulb"                 -> Icons.Rounded.Lightbulb
        "lamp.ceiling"              -> Icons.Rounded.Light
        "power"                     -> Icons.Rounded.PowerSettingsNew
        "powercord"                 -> Icons.Rounded.ElectricalServices
        "thermometer"               -> Icons.Rounded.Thermostat
        "tv"                        -> Icons.Rounded.Tv
        "fan"                       -> Icons.Rounded.WindPower
        else                        -> Icons.Rounded.DeviceUnknown
    }
    val names = listOf(
        "wind", "exclamationmark.triangle","cup.and.saucer","speedometer",
        "theatermasks", "scope", "drop", "lightbulb","lamp.ceiling",
        "power", "powercord", "thermometer", "tv", "fan"
    )

}