package com.capa1.switchcontrol.data.model

// IconMapper.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Light
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconMapper {
    fun fromName(name: String): ImageVector = when (name) {
        "air"                -> Icons.Rounded.Air
        "cafe"               -> Icons.Rounded.Coffee
        "lamp"               -> Icons.Rounded.Light
        "lightbulb"          -> Icons.Rounded.Lightbulb
        "lock"               -> Icons.Rounded.Speed
        "On"                 -> Icons.Rounded.PowerSettingsNew
        "power"            -> Icons.Rounded.ElectricalServices
        "tv"                 -> Icons.Rounded.Tv
        "thermometer"        -> Icons.Rounded.Thermostat
        "tree"               -> Icons.Rounded.Forest
        "filter"             -> Icons.Rounded.FilterAlt
        "drop"               -> Icons.Rounded.WaterDrop
        "pump"               -> Icons.Rounded.Water
        "fan"                -> Icons.Rounded.WindPower
        "speaker"            -> Icons.Rounded.Speaker
        "fun"                -> Icons.Rounded.TheaterComedy
        else                 -> Icons.Rounded.DeviceUnknown
    }

    fun getAllIcons(): List<Pair<String, ImageVector>> {
        val names = listOf(
            "wind", "exclamationmark.triangle","cup.and.saucer","speedometer",
            "theatermasks", "scope", "drop", "lightbulb","lamp.ceiling",
            "power", "powercord", "thermometer", "tv", "fan"
        )
        return names.map { it to fromName(it) }
    }
}