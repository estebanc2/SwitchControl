package com.capa1.switchcontrol.data.model

data class ScreenData(
    var name: String,
    val id: String,
    var icon: String,
    var timerInfo: String,
    var swOn: Boolean,
    var connected: Boolean
)