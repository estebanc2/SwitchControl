package com.capa1.switchcontrol.data.model

data class ConfigurableData(
    var name:String,
    var mode: Int,
    var secs: Int,
    val prgs: MutableList<WeeklyProgram>,
    var bkColor:String,
    var row: Int
)
