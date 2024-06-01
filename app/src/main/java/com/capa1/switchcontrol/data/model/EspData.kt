package com.capa1.switchcontrol.data.model

data class EspData(
    val name: String,
    val state: Int, //SwState,
    val mode: Int, //SwMode,
    val secs: Int,
    val prgs: MutableList<WeeklyProgram>,
    val tempX10:Int
)

data class WeeklyProgram(
    val start: Int,
    val stop: Int,
    val days: Int
)

enum class SwMode {
    TIMERS,
    TIMERS_CONTACT,
    PULSE_NA,
    PULSE_NC,
    TIMERS_TEMP,
    TEMP
}
