package com.capa1.switchcontrol.data.model

import android.content.Context
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.Global.SEND_GET
import com.capa1.switchcontrol.data.Global.SEND_OFF
import com.capa1.switchcontrol.data.Global.SEND_ON
import com.capa1.switchcontrol.data.SwDataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeepSwData(context: Context, private val listener: KeepSwDataListener) {
    private var swList = listOf<SwData>()
    private var swMap = mutableMapOf<String, EspData>()
    private val allSwId = "000"
    private var swScreenMap = mutableMapOf<String, SwScreenData>()
    private val swDataStore = SwDataStore(context)
    private val something = listOf(
        SwData("velador", "00AB", 1, 1, SwStatus.DISCONNECTED),
        SwData("luz cocina", "10AB", 2, 1, SwStatus.DISCONNECTED),
        SwData("riego", "20AB", 3, 1, SwStatus.DISCONNECTED),
        SwData("TV", "30AB", 4, 1, SwStatus.DISCONNECTED)
    )

    fun newMsg(id: String, newEspData: EspData) {
        for (data in swList){
            if (data.id == id){
                swMap[id] = newEspData
                swScreenMap[id] = SwScreenData(getSwImage(id), getLeyend(id))
                data.status = SwStatus.CONNECTED
            } else if(allSwId == id){
                //todo
            }
        }
    }

    private fun getLeyend(id: String): String{
        return "frafra"
    }

    private fun getSwImage (id: String): SwImages{
        
        return SwImages.CLOSE_LOCK
    }

    fun actualizeSwList(){
        swList = something //getStoredSwList()
        listener.notifySwList(swList)
        initializeSwList(swList)
    }
    private fun initializeSwList (swList: List<SwData>) {
        for(sw in swList){
            listener.subscribe(sw.id)
            initSw(sw.id)
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

    private fun initSw( id: String){
        listener.publish(id, SEND_GET)
    }

    fun imageClick(id: String) {
        for (data in swList) {
            if (data.id == id) {
                if (data.status == SwStatus.CONNECTED) {
                    data.status = SwStatus.CONNECTING
                    when ( swMap[id]?.state ?: SwState.GET_DATA.ordinal) {
                        SwState.OFF.ordinal -> listener.publish(id, SEND_ON)
                        SwState.ON.ordinal -> listener.publish(id, SEND_OFF)
                    }
                }
            }
        }
    }

}