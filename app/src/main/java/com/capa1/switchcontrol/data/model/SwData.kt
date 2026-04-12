package com.capa1.switchcontrol.data.model

data class SwData(
    var name: String,
    var state: State, //SwState,
    var mode: Mode, //SwMode,
    var secs: Int,
    var prgs: MutableList<WeeklyProgram>,
    var tempX10:Int,
    var icon: String,
    var row: Int,
    var status: SwStatus
)
