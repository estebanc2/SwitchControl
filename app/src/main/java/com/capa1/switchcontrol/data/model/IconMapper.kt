package com.capa1.switchcontrol.data.model

// IconMapper.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconMapper {
    fun fromName(name: String): ImageVector = when (name) {
        "air"                -> Icons.Rounded.Air
        "bluetooth"          -> Icons.Rounded.Bluetooth
        "lamp"               -> Icons.Rounded.Light
        "lightbulb"          -> Icons.Rounded.Lightbulb
        "lock"               -> Icons.Rounded.Lock
        "On"                 -> Icons.Rounded.PowerSettingsNew
        "power"              -> Icons.Rounded.Power
        "speaker"            -> Icons.Rounded.Speaker
        "tv"                 -> Icons.Rounded.Tv
        "thermometer"        -> Icons.Rounded.Thermostat
        "tree"               -> Icons.Rounded.Forest
        "filter"             -> Icons.Rounded.FilterAlt
        "drop"               -> Icons.Rounded.WaterDrop
        "pump"               -> Icons.Rounded.Water
        "fan"                -> Icons.Rounded.WindPower
        else                 -> Icons.Rounded.DeviceUnknown
    }

    fun getAllIcons(): List<Pair<String, ImageVector>> {
        val names = listOf(
            "air", "bluetooth", "lamp", "lightbulb", "lock",
            "On", "power", "speaker", "tv", "thermometer",
            "tree", "filter", "drop", "pump", "fan"
        )
        return names.map { it to fromName(it) }
    }
}