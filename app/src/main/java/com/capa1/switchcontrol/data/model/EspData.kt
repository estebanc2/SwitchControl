package com.capa1.switchcontrol.data.model

data class EspData(
    val topic: Int,
    val name: String,
    val state: SwState,
    val mode: Mode,
    val modeAux: Int,
    val tempX10: Int,
    val swProgs: List<WeeklyProgram>
)

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
enum class Mode {
    TIMERS,
    TIMERS_CONTACT,
    PULSE_NA,
    PULSE_NC,
    TIMERS_TEMP,
    TEMP
}
