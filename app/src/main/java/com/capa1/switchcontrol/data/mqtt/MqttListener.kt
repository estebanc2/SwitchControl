package com.capa1.switchcontrol.data.mqtt

interface MqttListener {
    fun notifyNewMessage(id: String, msg: String)
    fun notifyMqttState(mqttState: MqttState)
    fun notifySubscribed(id:String)
}