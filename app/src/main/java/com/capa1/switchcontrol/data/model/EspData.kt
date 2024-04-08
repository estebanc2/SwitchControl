package com.capa1.switchcontrol.data.model

data class EspData(
    val name: String,
    val state: Int, //SwState,
    val mode: Int, //SwMode,
    val modeAux: Int,
    val swPrograms: List<WeeklyProgram>,
    val tempX10:Int
)
