package com.capa1.switchcontrol.data.model

import android.content.Context
import android.util.Log
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.Global.SEND_GET
import com.capa1.switchcontrol.data.Global.SEND_OFF
import com.capa1.switchcontrol.data.Global.SEND_ON
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.SwDataStore
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import com.capa1.switchcontrol.data.wifi.EspTouch
import com.capa1.switchcontrol.data.wifi.WifiCredentials
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val wifiCredentials = WifiCredentials(context)
    private val espTouch = EspTouch(context)
    private var swList = mutableListOf<SwData>()
    var swMap = mutableMapOf<String, EspData>()
    val allSwId = "abc12345678f"
    private var newSwId = ""
    val swScreenList: MutableStateFlow<List<SwScreenData>> = MutableStateFlow(listOf())
    private val swDataStore = SwDataStore(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var mqttUp = false

    override fun notifyNewMessage(id: String, msg: String) {
        val gson = Gson()
        if (id == allSwId) {
            val newFlashData = gson.fromJson(msg, FlashData::class.java)
            swList = newFlashData.swList.toMutableList()
            saveData()
            initializeSwList()
        } else if (id == newSwId) {
            newSwId = ""
            val newEspData = gson.fromJson(msg, EspData::class.java)
            swMap[id] = newEspData
            swList += SwData(newEspData.name, id, (swList.size + 1), "nada", SwStatus.CONNECTED)
            saveData()
        } else {
            for (data in swList) {
                if (data.id == id) {
                    val newEspData = gson.fromJson(msg, EspData::class.java)
                    swMap[id] = newEspData
                    data.status = SwStatus.CONNECTED
                }
            }
        }
        Log.i(TAG, "mqtt received data : $msg")
        refreshScreenInfo()
    }

    override fun notifyMqttState(mqttState: MqttState) {
        if (mqttState == MqttState.UP) {
            mqttUp = true
            initializeSwList()
        }
    }

    fun initOperation() {
        mqttManager.connect()
        getStoredData()//something.sortedBy { it.row }.toMutableList() //
        refreshScreenInfo()
        Log.i(TAG, "ssid: ${wifiCredentials.mSsid}")
    }

    fun setSwWithId(id: String) {
        newSwId = id
        if (mqttUp) {
            mqttManager.subscribe(id)
            mqttManager.publish(id, SEND_GET)
        }
    }

    fun imageClick(id: String) {
        for (data in swList) {
            if (data.id == id) {
                if (data.status == SwStatus.CONNECTED) {
                    data.status = SwStatus.CONNECTING
                    when ( swMap[id]?.state ?: SwState.GET_DATA.ordinal) {
                        SwState.OFF.ordinal -> {
                            mqttManager.publish(id, SEND_ON)
                        }
                        SwState.ON.ordinal -> {
                            mqttManager.publish(id, SEND_OFF)
                        }
                    }
                }
            }
        }
    }

    fun configUpgrade(data: ConfigurableData, id:String){
        if (data.name != swMap[id]!!.name ||
            data.prgs != swMap[id]!!.prgs ||
            data.mode != swMap[id]!!.mode ||
            data.secs != swMap[id]!!.secs){
            val setData = Global.gson.toJson( EspData(
                data.name,
                SwState.SET_DATA.ordinal,
                data.mode,
                data.secs,
                data.prgs,
                0))
            mqttManager.publish(id,setData)
        }
        var newSwData = SwData("","", 1, "nada", SwStatus.DISCONNECTED)
        var newIndex = 0
        var aChange = false
        swList.forEachIndexed { index, swData ->
            if (swData.id == id) {
                newSwData = swData
                newIndex = index
            }
        }
        if(data.name != newSwData.name){
            swList[newIndex].name = data.name
            aChange = true
        }
        if(data.bkColor != newSwData.bkColor){
            swList[newIndex].bkColor = data.bkColor
            aChange = true
        }
        if(data.row != newSwData.row){
            swList.remove(newSwData)
            swList.add(data.row - 1, newSwData)
            swList.forEachIndexed { index, _  ->
                swList[index].row = index + 1
            }
            aChange = true
        }
        if(aChange){
            refreshScreenInfo()
            saveData()
            Log.i(TAG, "cambio algo en el swList de ${swList.size} elementos")
        }
    }

    fun sendConfig(id: String) {
        val allData = Global.gson.toJson( getStoredData())
        mqttManager.publish(id,allData)
    }

    private fun refreshScreenInfo() {
        val refreshList: MutableList<SwScreenData> = mutableListOf()
        swList.forEachIndexed { index, swData ->
            refreshList += SwScreenData(
                name = swData.name,
                id = swData.id,
                row = index + 1,
                bkColor = swData.bkColor,
                swImageId = getSwImageId(swData.id),
                timerInfo = getLegend(swData.id)
            )
        }
        coroutineScope.launch { swScreenList.emit(refreshList) }
    }

    private fun isSet( days: Int, position: Int): Boolean {
        return days shr position and 1 == 1
    }

    private fun getLegend(id: String): String {
        if (!swMap.containsKey(id)) {
            return "Sin Información"
        }
        when (swMap[id]!!.mode) {
            SwMode.PULSE_NA.ordinal -> return "Pulso de ${swMap[id]!!.secs} segundos"
            SwMode.PULSE_NC.ordinal -> return "Pulso de ${swMap[id]!!.secs} segundos"
            SwMode.TEMP.ordinal -> return "Enciende si temp < $${swMap[id]!!.tempX10 / 10}°"
            else -> {
                if (swMap[id]!!.state != SwState.OFF.ordinal &&
                    swMap[id]!!.state != SwState.ON.ordinal
                ) {
                    return "Sin información"
                }
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val min = calendar.get(Calendar.MINUTE)
                val today = calendar.get(Calendar.DAY_OF_WEEK) - 1
                val tomorrow = (today + 1) % 7
                val rightNow = (hour * 60) + min
                var delta = 24 * 60
                if (swMap[id]!!.state == SwState.OFF.ordinal) {
                    for (prg in swMap[id]!!.prgs) {
                        if (isSet(prg.days, today)) {
                            val deltaTrans = prg.start - rightNow
                            if (deltaTrans in 1..<delta) {
                                delta = deltaTrans
                            }
                        }
                        if (isSet(prg.days, tomorrow)) {
                            val deltaTrans = prg.start + 24 * 60 - rightNow
                            if (deltaTrans < delta) {
                                delta = deltaTrans
                            }
                        }
                    }
                } else {
                    for (prg in swMap[id]!!.prgs) {
                        if (isSet(prg.days, today)) {
                            val deltaTrans = prg.stop - rightNow
                            if (deltaTrans in 1..<delta) {
                                delta = deltaTrans
                            }
                        }
                        if (isSet(prg.days, tomorrow)) {
                            val deltaTrans = prg.stop + 24 * 60 - rightNow
                            if (deltaTrans < delta) {
                                delta = deltaTrans
                            }
                        }
                    }
                }
                val deltaHours = delta / 60
                val deltaMin = String.format(Locale.ENGLISH, "%02d", delta % 60)
                val tempText = if (swMap[id]!!.mode == SwMode.TIMERS_TEMP.ordinal) {
                    " si temp < ${swMap[id]!!.secs / 10}°. Actual: ${swMap[id]!!.tempX10 / 10}"
                } else {
                    ""
                }
                return if (deltaHours < 24) {
                    "Cambia en $deltaHours:$deltaMin h$tempText"
                } else {
                    "Cambia en más de 24 h$tempText"
                }
            }
        }
    }

    private fun getSwImageId(id: String): Int {
        if (!swMap.containsKey(id)) {
            return R.drawable.no_info
        }
        when (swMap[id]!!.mode to swMap[id]!!.state) {
            (SwMode.TIMERS.ordinal to SwState.ON.ordinal),
            (SwMode.TIMERS_TEMP.ordinal to SwState.ON.ordinal),
            (SwMode.TIMERS_CONTACT.ordinal to SwState.ON.ordinal) -> {
                return R.drawable.close
            }

            (SwMode.TIMERS.ordinal to SwState.OFF.ordinal),
            (SwMode.TIMERS_TEMP.ordinal to SwState.OFF.ordinal),
            (SwMode.TIMERS_CONTACT.ordinal to SwState.OFF.ordinal) -> {
                return R.drawable.open
            }

            (SwMode.PULSE_NA.ordinal to SwState.ON.ordinal) -> return R.drawable.na
            (SwMode.PULSE_NA.ordinal to SwState.OFF.ordinal) -> return R.drawable.nc
            (SwMode.PULSE_NC.ordinal to SwState.ON.ordinal) -> return R.drawable.nc
            (SwMode.PULSE_NC.ordinal to SwState.OFF.ordinal) -> return R.drawable.na
            (SwMode.TEMP.ordinal to SwState.ON.ordinal) -> return R.drawable.close_lock
            (SwMode.TEMP.ordinal to SwState.OFF.ordinal) -> return R.drawable.open_lock
            else -> return R.drawable.no_info
        }
    }

    private fun initializeSwList() {
        for (sw in swList) {
            mqttManager.subscribe(sw.id)
            initSw(sw.id)
        }
        checkSwitches()
    }

    private fun getStoredData() {
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getFlashData.collect { flashData ->
                val gson = Gson()
                swList = gson.fromJson(flashData, FlashData::class.java).swList
            }
        }
    }

    private fun saveData() {
        val gson = Gson()
        val flashData = gson.toJson(FlashData("version 0", swList))
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.saveFlashData(flashData)
        }
    }

    private fun checkSwitches() {
        val timerInSec = 10L
        fixedRateTimer("timer", false, 0L, timerInSec * 1000) {
            for (sw in swList) {
                if (sw.status == SwStatus.DISCONNECTED) {
                    initSw(sw.id)
                    Log.i(TAG, "------- ${sw.id} esta DESCONECTADO")
                }
            }
        }
    }

    private fun initSw(id: String) {
        mqttManager.publish(id, SEND_GET)
    }
}