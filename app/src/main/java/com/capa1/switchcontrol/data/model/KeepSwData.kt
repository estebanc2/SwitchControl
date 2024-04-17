package com.capa1.switchcontrol.data.model

import android.content.Context
import com.capa1.switchcontrol.data.Global.SEND_GET
import com.capa1.switchcontrol.data.Global.SEND_OFF
import com.capa1.switchcontrol.data.Global.SEND_ON
import com.capa1.switchcontrol.data.SwDataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.pow

class KeepSwData(context: Context, private val listener: KeepSwDataListener) {
    private var swList = listOf<SwData>()
    private var swMap = mutableMapOf<String, EspData>()
    private val allSwId = "000"
    private var swScreenMap = mutableMapOf<String, SwScreenData>()
    private val swDataStore = SwDataStore(context)
    private val something = listOf(
        SwData("velador", "00AB", 1, "nada", SwStatus.DISCONNECTED),
        SwData("luz cocina", "10AB", 2, "metal", SwStatus.DISCONNECTED),
        SwData("riego", "20AB", 3, "limon", SwStatus.DISCONNECTED),
        SwData("TV", "30AB", 4, "palta", SwStatus.DISCONNECTED)
    )

    fun newMsg(id: String, newEspData: EspData) {
        for (data in swList){
            if (data.id == id){
                swMap[id] = newEspData
                data.status = SwStatus.CONNECTED
                swScreenMap[id] = SwScreenData(getSwImage(id), getLegend(id))

            } else if(allSwId == id){
                //todo
            }
        }
    }

    private fun getLegend(id: String): String{
        when(swMap[id]!!.mode){
            SwMode.PULSE_NA.ordinal -> return "Pulso de ${swMap[id]!!.modeAux} segundos"
            SwMode.PULSE_NC.ordinal -> return "Pulso de ${swMap[id]!!.modeAux} segundos"
            SwMode.TEMP.ordinal -> return "Enciende si temp < $${swMap[id]!!.tempX10/10}°"
            else -> {
                if(swMap[id]!!.state != SwState.OFF.ordinal &&
                    swMap[id]!!.state != SwState.ON.ordinal){
                    return "Sin información"
                }
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val min = calendar.get(Calendar.MINUTE)
                val today = calendar.get(Calendar.DAY_OF_WEEK)
                val tomorrow = (today + 1) % 7
                val rightNow = (hour * 60) + min
                var delta = 24 * 60
                if (swMap[id]!!.state == SwState.OFF.ordinal){
                    for (prg in swMap[id]!!.swPrograms){
                        if( 2.0.pow(today.toDouble()) != 0.0 && prg.days != 0){
                            val deltaTrans = prg.start - rightNow
                            if(deltaTrans in 1..<delta){
                                delta = deltaTrans
                            }
                        }
                        if( 2.0.pow(tomorrow.toDouble()) != 0.0 && prg.days != 0){
                            val deltaTrans = prg.start + 24 * 60 - rightNow
                            if(deltaTrans < delta){
                                delta = deltaTrans
                            }
                        }
                    }
                }
                else{
                    for (prg in swMap[id]!!.swPrograms){
                        if( 2.0.pow(today.toDouble()) != 0.0 && prg.days != 0){
                            val deltaTrans = prg.stop - rightNow
                            if(deltaTrans in 1..<delta){
                                delta = deltaTrans
                            }
                        }
                        if( 2.0.pow(tomorrow.toDouble()) != 0.0 && prg.days != 0){
                            val deltaTrans = prg.stop + 24 * 60 - rightNow
                            if(deltaTrans < delta){
                                delta = deltaTrans
                            }
                        }
                    }
                }
                val deltaHours = delta / 60
                val deltaMin =  String.format(Locale.ENGLISH, "%02d", delta % 60)
                val tempText = if(swMap[id]!!.mode == SwMode.TIMERS_TEMP.ordinal){
                        " si temp < ${swMap[id]!!.modeAux / 10}°. Actual: ${swMap[id]!!.tempX10 / 10}"
                    }
                    else{ "" }
                if (deltaHours < 24){
                    return "Cambia en $deltaHours:$deltaMin h$tempText"
                }
                else{
                    return "Cambia en más de 24 h$tempText"
                }
            }
        }
    }

    private fun getSwImage (id: String): SwImages{
        when(swMap[id]!!.mode to swMap[id]!!.state){
            (SwMode.TIMERS.ordinal to SwState.ON.ordinal),
            (SwMode.TIMERS_TEMP.ordinal to SwState.ON.ordinal),
            (SwMode.TIMERS_CONTACT.ordinal to SwState.ON.ordinal)-> {
                return SwImages.CLOSE
            }
            (SwMode.TIMERS.ordinal to SwState.OFF.ordinal),
            (SwMode.TIMERS_TEMP.ordinal to SwState.OFF.ordinal),
            (SwMode.TIMERS_CONTACT.ordinal to SwState.OFF.ordinal)-> {
                return SwImages.OPEN
            }
            (SwMode.PULSE_NA.ordinal to SwState.ON.ordinal) -> return SwImages.NC
            (SwMode.PULSE_NA.ordinal to SwState.OFF.ordinal) -> return SwImages.NA
            (SwMode.PULSE_NC.ordinal to SwState.ON.ordinal) -> return SwImages.NA
            (SwMode.PULSE_NC.ordinal to SwState.OFF.ordinal) -> return SwImages.NC
            (SwMode.TEMP.ordinal to SwState.ON.ordinal) -> return SwImages.CLOSE_LOCK
            (SwMode.TEMP.ordinal to SwState.OFF.ordinal) -> return SwImages.OPEN_LOCK
            else -> return SwImages.NO_INFO
        }
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
                        SwState.OFF.ordinal -> {
                            listener.publish(id, SEND_ON)
                            swScreenMap[id]!!.swImage = SwImages.OPENING
                        }
                        SwState.ON.ordinal -> {
                            listener.publish(id, SEND_OFF)
                            swScreenMap[id]!!.swImage = SwImages.CLOSING
                        }
                    }
                }
            }
        }
    }

}