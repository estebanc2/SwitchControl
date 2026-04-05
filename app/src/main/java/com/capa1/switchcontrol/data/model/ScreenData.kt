package com.capa1.switchcontrol.data.model

data class ScreenData(
    val name: String,
    val id: String,
    val icon: String,
    val timerInfo: String,
    val swOn: Boolean,
    val connected: Boolean
)