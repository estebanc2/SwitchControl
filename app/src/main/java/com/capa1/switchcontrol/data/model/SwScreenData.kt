package com.capa1.switchcontrol.data.model

data class SwScreenData(
    val name: String,
    val id: String,
    val icon: String,
    val timerInfo: String,
    val isOn: Boolean,
    val connected: Boolean
)

val initScreen = SwScreenData(
    name = "Interruptor 1",
    id = "",
    icon = "tv.fill",
    timerInfo = "Sin informacion",
    isOn = false,
    connected = false
)