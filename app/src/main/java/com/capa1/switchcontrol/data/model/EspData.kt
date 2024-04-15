package com.capa1.switchcontrol.data.model

import com.capa1.switchcontrol.data.Global
import com.google.gson.Gson

data class EspData(
    val name: String,
    val state: Int, //SwState,
    val mode: Int, //SwMode,
    val modeAux: Int,
    val swPrograms: List<WeeklyProgram>,
    val tempX10:Int
) {
    val sendOn = Global.gson.toJson(
        EspData(
            name = "",
            state = SwState.ON.ordinal,
            mode = SwMode.TIMERS.ordinal,
            modeAux = 0,
            swPrograms = listOf(
                WeeklyProgram(0, 0, 0),
                WeeklyProgram(0, 0, 0),
                WeeklyProgram(0, 0, 0),
                WeeklyProgram(0, 0, 0)
            ),
            tempX10 = 0
        )
    )
}
