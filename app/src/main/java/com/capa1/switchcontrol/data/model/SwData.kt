package com.capa1.switchcontrol.data.model

data class SwData(
    val name: String,
    val id: String,
    val row: Int,
    val bkColor: Int,
    var status: SwStatus
)
