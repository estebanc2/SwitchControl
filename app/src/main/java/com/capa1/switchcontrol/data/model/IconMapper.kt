package com.capa1.switchcontrol.data.model

// IconMapper.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Light
import androidx.compose.material.icons.rounded.*
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