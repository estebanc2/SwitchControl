package com.capa1.switchcontrol.data.model

data class ConfigurableData(
    val name:String,
    val mode: Int,
    val secs: Int,
    val prgs: List<WeeklyProgram>,
    var bkColor:String,
    var row: Int,
    val timersInfo: List<String>
)
