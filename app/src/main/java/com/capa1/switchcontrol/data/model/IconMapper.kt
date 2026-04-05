package com.capa1.switchcontrol.data.model

// IconMapper.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconMapper {
    fun fromName(name: String): ImageVector = when (name) {
        "drop"               -> Icons.Rounded.WaterDrop
        "bolt"               -> Icons.Rounded.Bolt
        "bluetooth"          -> Icons.Rounded.Bluetooth
        "lightbulb"          -> Icons.Rounded.Lightbulb
        "lock"               -> Icons.Rounded.Lock
        "fan"                -> Icons.Rounded.Air
        "thermometer"        -> Icons.Rounded.Thermostat
        "power"              -> Icons.Rounded.Power
        "camera"             -> Icons.Rounded.PhotoCamera
        "speaker"            -> Icons.Rounded.Speaker
        "tv"                 -> Icons.Rounded.Tv
        "pump"               -> Icons.Rounded.HeatPump
        "lamp.desk"         -> Icons.Rounded.Desk
        "tree"               -> Icons.Rounded.AccountTree
        else                 -> Icons.Rounded.DeviceUnknown
    }
}