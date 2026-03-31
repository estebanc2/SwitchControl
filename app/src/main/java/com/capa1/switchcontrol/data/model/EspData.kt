package com.capa1.switchcontrol.data.model

import com.capa1.switchcontrol.data.Global.gson

enum class Mode {
    TIMERS,
    TIMERS_CONTACT,
    PULSE_NA,
    PULSE_NC,
    TIMERS_TEMP,
    TEMP
}

enum class SwState {
    OFF,
    ON,
    GET_DATA,
    SET_DATA,
    ERASE,
    UPGRADE,
    SERVER_FAIL,
    UPGRADE_FAIL,
    UPGRADED
}

data class WeeklyProgram(
    val start: Int,
    val stop: Int,
    val days: Int
)

data class EspData(
    val name: String,
    val state: Int, //SwState,
    val mode: Int, //SwMode,
    val secs: Int,
    val prgs: MutableList<WeeklyProgram>,
    val tempX10:Int
)

val NO_TIMERS: MutableList<WeeklyProgram> = mutableListOf ( WeeklyProgram(0,0,0),
    WeeklyProgram(0,0,0),
    WeeklyProgram(0,0,0),
    WeeklyProgram(0,0,0)
)

val SEND_ON: String = gson.toJson(
    EspData(
        name = "",
        state = SwState.ON.ordinal,
        mode = Mode.TIMERS.ordinal,
        secs = 0,
        prgs = NO_TIMERS,
        tempX10 = 0)
)
val SEND_OFF: String = gson.toJson(
    EspData(
        name = "",
        state = SwState.OFF.ordinal,
        mode = Mode.TIMERS.ordinal,
        secs = 0,
        prgs =  NO_TIMERS,
        tempX10 = 0)
)
val SEND_GET: String = gson.toJson(
    EspData(
        name = "",
        state = SwState.GET_DATA.ordinal,
        mode = Mode.TIMERS.ordinal,
        secs = 0,
        prgs =  NO_TIMERS,
        tempX10 = 0)
)
val SEND_ERASE: String = gson.toJson(
    EspData(
        name = "",
        state = SwState.ERASE.ordinal,
        mode = Mode.TIMERS.ordinal,
        secs = 0,
        prgs =  NO_TIMERS,
        tempX10 = 0)
)

