package com.capa1.switchcontrol.data

import android.content.Context
import android.util.Log
import com.capa1.switchcontrol.data.Global.FROM_SW
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.Global.TO_SW
import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.FlashData
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwMode
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class Controller @Inject constructor (context: Context) : MqttListener {
    private val mqttManager = MqttManager(this)
    private lateinit var initialList : List<SwData>
    val swList:   MutableStateFlow<List<SwData>> = MutableStateFlow(listOf())
    val swScreenMap: MutableStateFlow<Map<String, SwScreenData>> = MutableStateFlow(mapOf())
    val swMap: MutableStateFlow<Map<String, EspData>> = MutableStateFlow(mapOf())
    private val swDataStore = SwDataStore(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    init{
        initMqtt()
        loadSomething()
        actualizeSwList(getStoredSwList())
    }
    private fun actualizeSwList( newSwList: List<SwData>){
        initializeSwList(newSwList)
        coroutineScope.launch { swList.emit(newSwList) }
    }

    private fun initializeSwList (swList: List<SwData>) {
        for(sw in swList){
            subscribeToTopic(sw.topic)
            initSw(sw.topic)
        }
    }


    private fun loadSomething() {
        val something = listOf(
           SwData("velador", "00AB", 1, 1, SwStatus.DISCONNECTED),
           SwData("luz cocina", "10AB", 2, 1, SwStatus.DISCONNECTED),
           SwData("riego", "20AB", 3, 1, SwStatus.DISCONNECTED),
           SwData("TV", "30AB", 4, 1, SwStatus.DISCONNECTED)
        )
        saveSwList(something)
    }
    private fun getStoredSwList() : List<SwData> {
        var myList = FlashData("", emptyList())
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getList.collect { flashData ->
                val gson = Gson()
                myList = gson.fromJson(flashData, FlashData::class.java)
            }
        }
        return myList.swList
    }
    private fun saveSwList (listToSave: List<SwData>) {
        val flashData = FlashData("version 0", listToSave)
        val gson = Gson()
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.saveList(gson.toJson(flashData))
        }
    }

    fun subscribeToTopic(topic: String) {
        mqttManager.subscribe(FROM_SW + topic)
    }

    private fun initMqtt() {
        mqttManager.connect(context)
    }

    private fun initSw( topic: String){
        val gson = Gson()
        mqttManager.publish(TO_SW + topic, gson.toJson(
            EspData(
               "",
                SwState.GET_DATA.ordinal,
                SwMode.TIMERS.ordinal,
               0,
                listOf( WeeklyProgram(0,0,0),
                        WeeklyProgram(0,0,0),
                        WeeklyProgram(0,0,0),
                        WeeklyProgram(0,0,0)
                ),
               0)
            )
        )
    }

    override fun notifyNewMessage(topic: String, msg: String) {
        val gson = Gson()
        val newEspData = gson.fromJson(msg, EspData::class.java)
    }

    override fun notifyMqttState(mqttState: MqttState) {
        Log.i(TAG, "$mqttState")
    }

}