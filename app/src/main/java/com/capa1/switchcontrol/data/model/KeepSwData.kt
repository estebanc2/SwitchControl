package com.capa1.switchcontrol.data.model

import android.content.Context
import android.util.Log
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.Global.NO_TIMERS
import com.capa1.switchcontrol.data.Global.SEND_ERASE
import com.capa1.switchcontrol.data.Global.SEND_GET
import com.capa1.switchcontrol.data.Global.SEND_OFF
import com.capa1.switchcontrol.data.Global.SEND_ON
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.SwDataStore
import com.capa1.switchcontrol.data.mqtt.MqttListener
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import com.capa1.switchcontrol.data.wifi.ApData
import com.capa1.switchcontrol.data.wifi.EspTouch
import com.capa1.switchcontrol.data.wifi.TouchState
import com.capa1.switchcontrol.data.wifi.WifiCredentials
import com.capa1.switchcontrol.data.wifi.WifiListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random
import kotlin.collections.mutableListOf

class KeepSwData @Inject constructor (
    context: Context
): MqttListener, WifiListener {
    private val mqttManager = MqttManager(this)
    private val wifiCredentials = WifiCredentials(context, this)
    private val espTouch = EspTouch(context, this)
    private var newSw = mutableListOf<String>()
    private var newSwId = ""
    private var upgradingId = ""
    private val swDataStore = SwDataStore(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var mqttUp = false
    private var currentSsid = ""
    private var currentBssid = ""
    private val swMap = mutableMapOf<String, SwData>()
    val allSwId = Random.nextInt(9).toString() + "0123456789"+ Random.nextInt(9).toString()
    val swScreenList: MutableStateFlow<List<SwScreenData>> = MutableStateFlow(listOf())
    val myApData: MutableStateFlow<ApData> = MutableStateFlow(ApData("no conectado","", false))
    val touchState: MutableStateFlow<TouchState> = MutableStateFlow(TouchState.IN_PROGRESS)
    val starter: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val upgradeState: MutableStateFlow<Int>  = MutableStateFlow(0)

    override fun notifyNewMessage(id: String, msg: String) {
        val gson = Gson()
        when (id) {
            allSwId -> {
                val toStore = gson.toJson(msg)
                CoroutineScope(Dispatchers.IO).launch {
                    swDataStore.saveFlashData(toStore)
                }
                mqttManager.unsubscribe(id)
                getStoredData()
                initializeSw()
            }
            newSwId -> {
                newSwId = ""
                newSw -= id
                val esp = gson.fromJson(msg, EspData::class.java)
                swMap[id] = SwData( esp.name,
                                    SwState.entries[esp.state],
                                    SwMode.entries[esp.mode],
                                    esp.secs,
                                    esp.prgs,
                                    "nada",
                                    swMap.size + 1,
                                    SwStatus.CONNECTED,
                                    esp.tempX10)
                saveData()
            }
            upgradingId ->{
                val result = gson.fromJson(msg, EspData::class.java).state
                Log.i(TAG,"during upgrade receive a state: $result")
                coroutineScope.launch {upgradeState.emit(result) }
                if (result == SwState.UPGRADED.ordinal){
                    upgradingId = ""
                }
            }
            else -> {
                val esp = gson.fromJson(msg, EspData::class.java)
                swMap[id] = SwData( esp.name,
                                    SwState.entries[esp.state],
                                    SwMode.entries[esp.mode],
                                    esp.secs,
                                    esp.prgs,
                                    swMap[id]!!.bkColor,
                                    swMap[id]!!.row,
                                    SwStatus.CONNECTED,
                                    esp.tempX10)
                if(swMap[id]!!.mode == SwMode.TIMERS_TEMP) {
                    val tempId = (if(id.substring(0,2).toInt(16) == 255)
                        254 else id.substring(0,2).toInt(16) + 1).toString(16) + id.substring(2,id.length)
                    if (!swMap.contains(tempId)) {
                        setSwWithId(tempId)
                    }
                }
            }
        }
        Log.i(TAG, "Rx msg: $msg")
        refreshScreenInfo()
    }
    override fun notifyMqttState(mqttState: MqttState) {
        if (mqttState == MqttState.UP) {
            mqttUp = true
            initializeSw()
        }
    }
    override fun notifySubscribed(id: String) {
        if( id != allSwId){
            initSw(id)
        }
    }
    override fun notifyApData(myAp: ApData) {
        coroutineScope.launch { myApData.emit(myAp) }
        currentSsid = myAp.ssid
        currentBssid = myAp.bssid
    }
    override fun notifyTouch(id: String, state: TouchState) {
        coroutineScope.launch { touchState.emit(state) }
        if(state == TouchState.READY){
            setSwWithId(id)
        }
    }

    fun initOperation() {
        wifiCredentials.get()
        mqttManager.connect()
        getStoredData()
    }
    fun setSwWithId(id: String) {
        if (!swMap.contains(id)) {
            newSwId = id
            newSw += id
            if (mqttUp) {
                mqttManager.subscribe(id)
            }
        }
    }
    fun imageClick(id: String) {
        if (swMap[id]?.status == SwStatus.CONNECTED) {
            swMap [id]?.status = SwStatus.CONNECTING
            when ( swMap[id]?.state ) {
                SwState.OFF -> {
                    mqttManager.publish(id, SEND_ON)
                }
                SwState.ON -> {
                    mqttManager.publish(id, SEND_OFF)
                }
                else -> {
                    swMap [id]?.status = SwStatus.CONNECTED
                }
            }
        }
    }
    fun configUpgrade(newData: SwData, id: String){
        Log.i(TAG,"llega ${newData.name} al keepSwData con bkcolor ${newData.bkColor}")
        Log.i(TAG,"y se comparara con el bkcolor ${swMap[id]!!.bkColor} que ya estaba en el map")
        if (newData.name != swMap[id]!!.name ||
            newData.prgs != swMap[id]!!.prgs ||
            newData.mode != swMap[id]!!.mode ||
            newData.secs != swMap[id]!!.secs){
            val setData = Global.gson.toJson( EspData(
                newData.name,
                SwState.SET_DATA.ordinal,
                newData.mode.ordinal,
                newData.secs,
                newData.prgs,
                swMap[id]!!.tempX10))
            mqttManager.publish(id,setData)
            Log.i(TAG,"CAMBIO!! id: $id, values: $setData")
        }
        var aChange = false
        if(newData.name != swMap[id]!!.name){
            swMap[id]!!.name = newData.name
            aChange = true
        }
        if(newData.bkColor != swMap[id]!!.bkColor){
            swMap[id]!!.bkColor = newData.bkColor
            aChange = true
        }
        val oldRow = swMap[id]!!.row
        if(oldRow > newData.row) {
            swMap.forEach { (id, swData) ->
                if (swData.row < oldRow && swData.row >= newData.row) {
                    swMap[id]!!.row += 1
                }
            }
            swMap[id]!!.row = newData.row
            aChange = true
        }
        if(oldRow < newData.row) {
            swMap.forEach { (id, swData) ->
                if (swData.row > oldRow && swData.row <= newData.row) {
                    swMap[id]!!.row -= 1
                }
            }
            swMap[id]!!.row = newData.row
            aChange = true
        }
        if(aChange){
            Log.i(TAG,"CAMBIO!! id: $id")
            refreshScreenInfo()
            saveData()
        }
    }
    fun getCurrentSwData( id: String): SwData {
        return swMap[id]!!
    }
    fun discoverSwitches(pass: String){
        espTouch.discover(currentSsid, currentBssid, pass)
    }
    fun firmwareUpgrade(id: String, server: String, port: String) {
        coroutineScope.launch {upgradeState.emit(SwState.UPGRADE.ordinal) }
        upgradingId = id
        val setData = Global.gson.toJson( EspData(
            name = server,
            state = SwState.UPGRADE.ordinal,
            mode = 0,
            secs = port.toInt(),
            prgs = NO_TIMERS,
            tempX10 = 0
        ))
        mqttManager.publish(id,setData)
    }
    fun localErase(id: String){
        val erasedRow = swMap[id]?.row
        swMap.remove(id)
        swMap.forEach { (key, value) ->
            if (value.row > erasedRow!!) {
                swMap[key]?.row = value.row - 1
            }
        }
        saveData()
        refreshScreenInfo()
    }
    fun fullErase(id:String){
        localErase(id)
        mqttManager.publish(id, SEND_ERASE)
        mqttManager.unsubscribe(id)
    }
    fun receiveConfigFromOtherPhone() {
        if (mqttUp) {
            mqttManager.subscribeFromPhone(allSwId)
        }
    }
    fun sendConfigToOtherPhone(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getFlashData.collect { flashData ->
                mqttManager.publish(id, flashData)
            }
        }
    }

    private fun refreshScreenInfo() {
        val refreshList: MutableList<SwScreenData> = mutableListOf()
        swMap.forEach { (id, swData) ->
            refreshList += SwScreenData(name = swData.name,
                                        id = id,
                                        row = swData.row,
                                        bkColor = swData.bkColor,
                                        swImageId = getSwImageId(id),
                                        timerInfo = getLegend(id)
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
            SwMode.PULSE_NA -> return "Pulso de ${swMap[id]!!.secs} segundos"
            SwMode.PULSE_NC -> return "Pulso de ${swMap[id]!!.secs} segundos"
            SwMode.TEMP -> return "Enciende si temp < ${swMap[id]!!.secs / 10}°"
            else -> {
                if (swMap[id]!!.state != SwState.OFF &&
                    swMap[id]!!.state != SwState.ON
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
                if (swMap[id]!!.state == SwState.OFF) {
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
                val tempText = if (swMap[id]!!.mode == SwMode.TIMERS_TEMP) {
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
            (SwMode.TIMERS to SwState.ON),
            (SwMode.TIMERS_TEMP to SwState.ON),
            (SwMode.TIMERS_CONTACT to SwState.ON) -> {
                return R.drawable.close
            }

            (SwMode.TIMERS to SwState.OFF),
            (SwMode.TIMERS_TEMP to SwState.OFF),
            (SwMode.TIMERS_CONTACT to SwState.OFF) -> {
                return R.drawable.open
            }

            (SwMode.PULSE_NA to SwState.ON) -> return R.drawable.na
            (SwMode.PULSE_NA to SwState.OFF) -> return R.drawable.nc
            (SwMode.PULSE_NC to SwState.ON) -> return R.drawable.nc
            (SwMode.PULSE_NC to SwState.OFF) -> return R.drawable.na
            (SwMode.TEMP to SwState.ON) -> return R.drawable.close_lock
            (SwMode.TEMP to SwState.OFF) -> return R.drawable.open_lock
            else -> return R.drawable.no_info
        }
    }
    private fun initializeSw() {
        for (id in swMap.keys) {
            mqttManager.subscribe(id)
        }
        checkSwitches()
    }
    private fun getStoredData() {
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getFlashData.collect { flashData ->
                val gson = Gson()
                val stored = gson.fromJson(flashData, ToStore::class.java)
                if (stored != null) {
                    if (stored.list.size == 0) {
                        coroutineScope.launch { starter.emit(true) }
                    } else {
                        stored.list.forEachIndexed { i, data ->
                            swMap[data.id] = SwData(name = data.name,
                                                    state = SwState.OFF,
                                                    mode = SwMode.TIMERS,
                                                    secs = 0,
                                                    prgs = NO_TIMERS,
                                                    bkColor = data.bkColor,
                                                    row = i + 1,
                                                    status = SwStatus.DISCONNECTED,
                                                    tempX10 = 0)
                        }
                        refreshScreenInfo()
                    }
                } else {
                    Log.i(TAG,"bad stored data!")
                }
            }
        }
    }
    private fun saveData() {
        val list = mutableListOf<StoredData>()
        swMap.forEach { (id, swData) ->
            list.add(StoredData(swData.name, id, swData.bkColor))
        }
        val gson = Gson()
        val toStore = gson.toJson(ToStore(list))
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.saveFlashData(toStore)
        }
    }
    private fun checkSwitches() {
        val timerInSec = 10L
        val initTimerInSec = 5L
        fixedRateTimer("timer", false, initTimerInSec * 1000, timerInSec * 1000) {
            swMap.forEach { (id, swData)->
                if (swData.status == SwStatus.DISCONNECTED) {
                    initSw(id)
                    Log.i(TAG,"id no connected: ${id})")
                }
            }
            for (id in newSw){
                initSw(id)
                Log.i(TAG,"id en lista de nuevos: ${id})")
            }
        }
    }
    private fun initSw(id: String) {
        mqttManager.publish(id, SEND_GET)
    }
 }