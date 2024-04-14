package com.capa1.switchcontrol.data.model

interface KeepSwDataListener {
    fun notifySwList(swList: List<SwData>)
    fun notifySwMap(swMap: Map<String, EspData>)
    fun notifySwScreenMap(swScreenMap: Map<String, SwScreenData>)
    fun subscribe(topic: String)
    fun publish(topic: String, msg: String)
}