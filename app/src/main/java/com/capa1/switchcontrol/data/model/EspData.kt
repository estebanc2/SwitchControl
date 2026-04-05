package com.capa1.switchcontrol.data.model

import com.capa1.switchcontrol.data.Global.gson

data class EspData(
    val name: String,
    val state: State, //SwState,
    val mode: Mode, //SwMode,
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
        state = State.ON,
        mode = Mode.TIMERS,
        secs = 0,
        prgs = NO_TIMERS,
        tempX10 = 0)
)
val SEND_OFF: String = gson.toJson(
    EspData(
        name = "",
        state = State.OFF,
        mode = Mode.TIMERS,
        secs = 0,
        prgs =  NO_TIMERS,
        tempX10 = 0)
)
val SEND_GET: String = gson.toJson(
    EspData(
        name = "",
        state = State.GET_DATA,
        mode = Mode.TIMERS,
        secs = 0,
        prgs =  NO_TIMERS,
        tempX10 = 0)
)
val SEND_ERASE: String = gson.toJson(
    EspData(
        name = "",
        state = State.ERASE,
        mode = Mode.TIMERS,
        secs = 0,
        prgs =  NO_TIMERS,
        tempX10 = 0)
)

