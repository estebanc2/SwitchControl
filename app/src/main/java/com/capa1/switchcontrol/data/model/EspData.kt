package com.capa1.switchcontrol.data.model

data class EspData(
    val topic: Int,
    val name: String,
    val state: SwState,
    val mode: SwMode,
    val modeAux: Int,
    val tempX10: Int,
    val swPrograms: List<WeeklyProgram>
)
