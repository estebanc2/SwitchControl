package com.capa1.switchcontrol.data.model

enum class SwStatus {
    CONNECTED,
    DISCONNECTED,
    CONNECTING
}

data class SwData(
    var name: String,
    var swOn: Boolean,
    var mode: Mode,
    var secs: Int,
    var prgs: MutableList<WeeklyProgram>,
    var tempX10: Int,
    var icon: String,
    var row: Int
)

