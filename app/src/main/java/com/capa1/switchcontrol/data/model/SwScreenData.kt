package com.capa1.switchcontrol.data.model

data class SwScreenData(
    val name: String,
    val id: String,
    val icon: String,
    val timerInfo: String,
    val swOn: Boolean,
    val connected: Boolean
)