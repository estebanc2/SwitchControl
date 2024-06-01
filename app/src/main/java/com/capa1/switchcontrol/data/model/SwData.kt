package com.capa1.switchcontrol.data.model

data class SwData(
    var name: String,
    var state: SwState,
    var mode: SwMode,
    var secs: Int,
    var prgs: MutableList<WeeklyProgram>,
    var tempX10: Int,
    var bkColor: String,
    var row: Int,
    var status: SwStatus
)

enum class SwStatus {
    CONNECTED,
    DISCONNECTED,
    CONNECTING
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
