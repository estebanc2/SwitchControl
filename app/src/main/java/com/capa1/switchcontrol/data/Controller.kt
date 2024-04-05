package com.capa1.switchcontrol.data

import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import kotlinx.coroutines.flow.MutableStateFlow

class Controller : MqttListener {
    private val mqttManager = MqttManager()
    val swList: MutableStateFlow<List<SwData>> = MutableStateFlow(listOf())

    fun getSwList(): List<SwData> {
        return listOf(
           SwData("velador", "00AB", 1, 1, SwStatus.DISCONNECTED),
           SwData("luz cocina", "10AB", 2, 1, SwStatus.DISCONNECTED),
           SwData("riego", "20AB", 3, 1, SwStatus.DISCONNECTED),
           SwData("TV", "30AB", 4, 1, SwStatus.DISCONNECTED)
        )
    }
    fun subscribeToTopic(topic: String) {

    }

    fun initMqtt() {
        TODO("Not yet implemented")
    }

    override fun notifyNewMessage(topic: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun notifyMqttState(mqttState: MqttState) {
        TODO("Not yet implemented")
    }

}