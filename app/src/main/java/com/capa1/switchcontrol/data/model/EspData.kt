package com.capa1.switchcontrol.data.model

data class EspData(
    val name: String,
    val state: State, //SwState,
    val mode: Mode, //SwMode,
    val secs: Int,
    val prgs: MutableList<WeeklyProgram>,
    val tempX10:Int
)

