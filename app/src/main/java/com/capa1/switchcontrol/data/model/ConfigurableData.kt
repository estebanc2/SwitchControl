package com.capa1.switchcontrol.data.model

import com.capa1.switchcontrol.data.Global.NO_TIMERS

data class ConfigurableData(
    var name:String,
    val mode: Int,
    val secs: Int,
    val prgs: List<WeeklyProgram>,
    var bkColor:String,
    var row: Int,
    val timersInfo: List<String>
)
