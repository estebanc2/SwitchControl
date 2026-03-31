package com.capa1.switchcontrol.data.model

enum class SwStatus {
    CONNECTED,
    DISCONNECTED,
    CONNECTING
}

data class SwData(
    var name: String,
    var state: SwState,
    var mode: Mode,
    var secs: Int,
    var prgs: MutableList<WeeklyProgram>,
    var tempX10: Int,
    var icon: String,
    var row: Int,
    var status: SwStatus
)

