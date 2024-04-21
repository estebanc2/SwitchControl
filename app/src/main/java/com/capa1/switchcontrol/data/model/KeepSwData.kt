package com.capa1.switchcontrol.data.model

import android.content.Context
import android.util.Log
import com.capa1.switchcontrol.data.Global.SEND_GET
import com.capa1.switchcontrol.data.Global.SEND_OFF
import com.capa1.switchcontrol.data.Global.SEND_ON
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.SwDataStore
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.math.pow

class KeepSwData @Inject constructor (
    context: Context
): MqttListener {
    private val mqttManager = MqttManager(this)
    private var _swList = listOf<SwData>()
    private var _swMap = mutableMapOf<String, EspData>()
    private val allSwId = "000"
    private val newSwId = "11111"
    private var _swScreenMap = mutableMapOf<String, SwScreenData>()
    val swList: MutableStateFlow<List<SwData>> = MutableStateFlow(listOf())
    val swScreenMap: MutableStateFlow<Map<String, SwScreenData>> = MutableStateFlow(mapOf())
    val swMap: MutableStateFlow<Map<String, EspData>> = MutableStateFlow(mapOf())
    private val swDataStore = SwDataStore(context)

    private val something = listOf(
        SwData("velador", "483fda877368", 2, "limon", SwStatus.DISCONNECTED),
        SwData("luz cocina", "98f4abb33d5a", 1, "lila", SwStatus.DISCONNECTED),
        SwData("riego", "483fda878e46", 3, "pasto", SwStatus.DISCONNECTED),
        SwData("alargue", "bcddc247dbc9", 5, "palta", SwStatus.DISCONNECTED),
        SwData("TV", "483fda879484", 4, "madera", SwStatus.DISCONNECTED)
    )
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun initOperation() {
        mqttManager.connect()
        _swList = something.sortedBy { it.row }.toMutableList() //getStoredData()
        coroutineScope.launch { swList.emit(_swList)}
    }
    override fun notifyNewMessage(id: String, msg: String) {
        val gson = Gson()
        if(id == allSwId) {
            val newFlashData = gson.fromJson(msg, FlashData::class.java)
            _swList = newFlashData.swList.sortedBy { it.row }.toMutableList()
            coroutineScope.launch { swList.emit(_swList) }
            saveData()
            initializeSwList()
        } else if (id == newSwId) {
            val newEspData = gson.fromJson(msg, EspData::class.java)
            _swMap[id] = newEspData
            _swList += SwData( newEspData.name, id,( _swList.size + 1 ) * 2,"nada", SwStatus.CONNECTED)
            _swScreenMap[id] = SwScreenData(getSwImage(id), getLegend(id))
            coroutineScope.launch { swList.emit(_swList) }
            coroutineScope.launch { swMap.emit(_swMap) }
            coroutineScope.launch { swScreenMap.emit(_swScreenMap) }
        } else {
            for (data in _swList) {
                if (data.id == id) {
                    val newEspData = gson.fromJson(msg, EspData::class.java)
                    _swMap[id] = newEspData
                    data.status = SwStatus.CONNECTED
                    _swScreenMap[id] = SwScreenData(getSwImage(id), getLegend(id))
                    Log.i(TAG, "------- ${id} HA CAMBIADO -----")
                    coroutineScope.launch { swList.emit(_swList) }
                    coroutineScope.launch { swMap.emit(_swMap) }
                    coroutineScope.launch { swScreenMap.emit(_swScreenMap) }
                }
            }
        }
    }

    override fun notifyMqttState(mqttState: MqttState) {
        if (mqttState == MqttState.UP){
            initializeSwList()
        }
    }

    private fun getLegend(id: String): String{
        when(_swMap[id]!!.mode){
            SwMode.PULSE_NA.ordinal -> return "Pulso de ${_swMap[id]!!.secs} segundos"
            SwMode.PULSE_NC.ordinal -> return "Pulso de ${_swMap[id]!!.secs} segundos"
            SwMode.TEMP.ordinal -> return "Enciende si temp < $${_swMap[id]!!.tempX10/10}°"
            else -> {
                if(_swMap[id]!!.state != SwState.OFF.ordinal &&
                    _swMap[id]!!.state != SwState.ON.ordinal){
                    return "Sin información"
                }
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val min = calendar.get(Calendar.MINUTE)
                val today = calendar.get(Calendar.DAY_OF_WEEK)
                val tomorrow = (today + 1) % 7
                val rightNow = (hour * 60) + min
                var delta = 24 * 60
                if (_swMap[id]!!.state == SwState.OFF.ordinal){
                    for (prg in _swMap[id]!!.prgs){
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
                    for (prg in _swMap[id]!!.prgs){
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
                val tempText = if(_swMap[id]!!.mode == SwMode.TIMERS_TEMP.ordinal){
                        " si temp < ${_swMap[id]!!.secs / 10}°. Actual: ${_swMap[id]!!.tempX10 / 10}"
                    }
                    else{ "" }
                return if (deltaHours < 24){
                    "Cambia en $deltaHours:$deltaMin h$tempText"
                } else{
                    "Cambia en más de 24 h$tempText"
                }
            }
        }
    }

    private fun getSwImage (id: String): SwImages{
        when(_swMap[id]!!.mode to _swMap[id]!!.state){
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
            (SwMode.PULSE_NA.ordinal to SwState.ON.ordinal) -> return SwImages.NA
            (SwMode.PULSE_NA.ordinal to SwState.OFF.ordinal) -> return SwImages.NC
            (SwMode.PULSE_NC.ordinal to SwState.ON.ordinal) -> return SwImages.NC
            (SwMode.PULSE_NC.ordinal to SwState.OFF.ordinal) -> return SwImages.NA
            (SwMode.TEMP.ordinal to SwState.ON.ordinal) -> return SwImages.CLOSE_LOCK
            (SwMode.TEMP.ordinal to SwState.OFF.ordinal) -> return SwImages.OPEN_LOCK
            else -> return SwImages.NO_INFO
        }
    }

    private fun initializeSwList () {
        for(sw in _swList){
            mqttManager.subscribe(sw.id)
            initSw(sw.id)
        }
        checkSwitches()
    }

    private fun getStoredData() : List<SwData> {
        var myList = FlashData("", emptyList())
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getList.collect { flashData ->
                val gson = Gson()
                myList = gson.fromJson(flashData, FlashData::class.java) ?: FlashData("", emptyList())
            }
        }
        return myList.swList
    }
    private fun saveData () {
        val flashData = FlashData("version 0", _swList)
        val gson = Gson()
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.saveList(gson.toJson(flashData))
        }
    }

    private fun checkSwitches(){
        val timerInSec = 10L
        fixedRateTimer("timer", false, 0L, timerInSec * 1000){

            coroutineScope.launch { swMap.emit(_swMap) }
            for (sw in _swList){
                if(sw.status == SwStatus.DISCONNECTED){
                    initSw(sw.id)
                    Log.i(TAG, "------- ${sw.id} esta DESCONECTADO")
                }
            }
        }
    }

    private fun initSw( id: String){
        mqttManager.publish(id, SEND_GET)
    }

    fun imageClick(id: String) {
        for (data in _swList) {
            if (data.id == id) {
                if (data.status == SwStatus.CONNECTED) {
                    data.status = SwStatus.CONNECTING
                    when ( _swMap[id]?.state ?: SwState.GET_DATA.ordinal) {
                        SwState.OFF.ordinal -> {
                            mqttManager.publish(id, SEND_ON)
                            _swScreenMap[id]!!.swImage = SwImages.OPENING
                        }
                        SwState.ON.ordinal -> {
                            mqttManager.publish(id, SEND_OFF)
                            _swScreenMap[id]!!.swImage = SwImages.CLOSING
                        }
                    }
                    coroutineScope.launch { swList.emit(_swList) }
                    coroutineScope.launch { swScreenMap.emit(_swScreenMap) }
                }
            }
        }
    }
}