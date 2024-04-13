package com.capa1.switchcontrol.ui

import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwScreenData

data class ScreenModifiers(
    val showList: Boolean = false,
    val swList: List<SwData> = listOf(),
    val swScreenMap: Map<String, SwScreenData> = mapOf(),
    val showConfig: Boolean = false
)