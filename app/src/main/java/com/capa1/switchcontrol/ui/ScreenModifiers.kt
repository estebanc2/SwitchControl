package com.capa1.switchcontrol.ui

import com.capa1.switchcontrol.data.model.SwData

data class ScreenModifiers(
    val showList: Boolean = false,
    val swList: List<SwData> = listOf(),
    val swInfo: List<String> = listOf()
)
