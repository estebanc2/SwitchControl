package com.capa1.switchcontrol.data.model

import android.content.Context
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.SwDataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeepSwData(context: Context, private val listener: KeepSwDataListener) {
    private var swList: List<SwData> = listOf()
    private val swDataStore = SwDataStore(context)
    private val something = listOf(
        SwData("velador", "00AB", 1, 1, SwStatus.DISCONNECTED),
        SwData("luz cocina", "10AB", 2, 1, SwStatus.DISCONNECTED),
        SwData("riego", "20AB", 3, 1, SwStatus.DISCONNECTED),
        SwData("TV", "30AB", 4, 1, SwStatus.DISCONNECTED)
    )



    fun newMsg(topic: String, newEspData: EspData) {
        TODO("Not yet implemented")
    }

    fun actualizeSwList(){
        val storedList = something //getStoredSwList()
        initializeSwList(storedList)
    }
    private fun initializeSwList (swList: List<SwData>) {
        for(sw in swList){
            subscribeToTopic(sw.topic)
            initSw(sw.topic)
        }
    }
    private fun getStoredSwList() : List<SwData> {
        var myList = FlashData("", emptyList())
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getList.collect { flashData ->
                val gson = Gson()
                myList = gson.fromJson(flashData, FlashData::class.java) ?: FlashData("", emptyList())
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
        listener.subscribe(Global.FROM_SW + topic)
    }

    private fun initSw( topic: String){
        val gson = Gson()
        listener.publish(
            Global.TO_SW + topic, gson.toJson(
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


}