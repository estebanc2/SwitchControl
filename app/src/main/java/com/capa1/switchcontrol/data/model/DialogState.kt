package com.capa1.switchcontrol.data.model

data class DialogState (
    val showName: Boolean = false,
    val showTimer: Boolean = false,
    val showAdd: Boolean = false,
    val showNewId: Boolean = false,
    val showNew: Boolean = false,
    val showAll: Boolean = false,
    val showMode: Boolean = false,
    val showIcon: Boolean = false,
    val showMaintenance: Boolean = false,
    val showConfig: Boolean = false,
    val mqttUp: Boolean = false,
)
