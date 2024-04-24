package com.capa1.switchcontrol.data.model

data class SwData(
    val name: String,
    val id: String,
    var row: Int,
    var bkColor: String = "nada",
    var status: SwStatus
)
enum class SwStatus {
    CONNECTED,
    DISCONNECTED,
    CONNECTING
}