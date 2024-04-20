package com.capa1.switchcontrol.ui

import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwScreenData

data class ScreenModifiers(
    val swList: List<SwData> = listOf(),
    val swMap: Map<String, EspData> = mapOf(),
    val swScreenMap: Map<String, SwScreenData> = mapOf(),
    val showConfig: Boolean = false,
    val showAdd: Boolean = false,
    val timersInfo: Map<String,List<String>> = mapOf(),

)