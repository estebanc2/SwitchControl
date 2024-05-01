package com.capa1.switchcontrol.data.model

data class EspData(
    val name: String,
    val state: Int, //SwState,
    val mode: Int, //SwMode,
    val secs: Int,
    val prgs: MutableList<WeeklyProgram>,
    val tempX10:Int
)