package com.capa1.switchcontrol.data.model

data class EspData(
    var name: String,
    var state: State, //SwState,
    var mode: Mode, //SwMode,
    var secs: Int,
    var prgs: MutableList<WeeklyProgram>,
    val tempX10:Int
)

