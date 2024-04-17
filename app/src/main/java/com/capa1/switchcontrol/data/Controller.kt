package com.capa1.switchcontrol.data

import android.content.Context
import android.util.Log
import com.capa1.switchcontrol.data.Global.FROM_SW
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.Global.TO_SW
import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.KeepSwData
import com.capa1.switchcontrol.data.model.KeepSwDataListener
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class Controller @Inject constructor (context: Context) : KeepSwDataListener, MqttListener {
    private val mqttManager = MqttManager(this)
    private val keepSwData = KeepSwData(context, this)
    val swList: MutableStateFlow<List<SwData>> = MutableStateFlow(listOf())
    val swScreenMap: MutableStateFlow<Map<String, SwScreenData>> = MutableStateFlow(mapOf())
    val swMap: MutableStateFlow<Map<String, EspData>> = MutableStateFlow(mapOf())
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun initOperation() {
        mqttManager.connect()
        keepSwData.actualizeSwList()
    }

    override fun notifySwList(swList: List<SwData>) {
        coroutineScope.launch { this@Controller.swList.emit(swList)}
    }

    override fun notifySwMap(swMap: Map<String, EspData>) {
        coroutineScope.launch { this@Controller.swMap.emit(swMap)}
    }

    override fun notifySwScreenMap(swScreenMap: Map<String, SwScreenData>) {
        coroutineScope.launch { this@Controller.swScreenMap.emit(swScreenMap)}
    }

    override fun subscribe(id: String) {
        val topic = FROM_SW + id
        mqttManager.subscribe(topic)
    }

    override fun publish(id: String, msg: String) {
        val topic = TO_SW + id
        mqttManager.publish(topic, msg)
    }

    fun imageClick(id: String){
        keepSwData.imageClick(id)
    }
    override fun notifyNewMessage(topic: String, msg: String) {
        val gson = Gson()
        val newEspData = gson.fromJson(msg, EspData::class.java)
        val id = topic.split("/").last()
        keepSwData.newMsg(id, newEspData)
    }

    override fun notifyMqttState(mqttState: MqttState) {
        Log.i(TAG, "$mqttState")
    }


}