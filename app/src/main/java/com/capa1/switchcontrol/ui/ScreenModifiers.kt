package com.capa1.switchcontrol.ui

import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwScreenData

data class ScreenModifiers(
    val showConfig: Boolean = false,
    val showAdd: Boolean = false,
    val timersInfo: Map<String,List<String>> = mapOf(),

)