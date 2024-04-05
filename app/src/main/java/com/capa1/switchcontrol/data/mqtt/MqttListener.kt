package com.capa1.switchcontrol.data.mqtt

interface MqttListener {
    fun notifyNewMessage(topic: String, msg: String)
    fun notifyMqttState(mqttState: MqttState)
}