package com.capa1.switchcontrol.data.model

import android.content.Context
import android.util.Log
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.Global.FLASH_VERSION
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

class KeepSwData @Inject constructor (
    context: Context
): MqttListener, WifiListener {
    private val mqttManager = MqttManager(this)
    private val wifiCredentials = WifiCredentials(context, this)
    private val espTouch = EspTouch(context, this)
    private var swList = mutableListOf<SwData>()
    private var newSw = mutableListOf<String>()
    private var newSwId = ""
    private val swDataStore = SwDataStore(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var mqttUp = false
    private var currentSsid = ""
    private var currentBssid = ""
    var swMap = mutableMapOf<String, EspData>()
    val allSwId = Random.nextInt(9).toString() + "0123456789"+ Random.nextInt(9).toString()
    val swScreenList: MutableStateFlow<List<SwScreenData>> = MutableStateFlow(listOf())
    val myApData: MutableStateFlow<ApData> = MutableStateFlow(ApData("no conectado","", false))
    val touchState: MutableStateFlow<TouchState> = MutableStateFlow(TouchState.IN_PROGRESS)
    val starter: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun notifyNewMessage(id: String, msg: String) {
        val gson = Gson()
        when (id) {
            allSwId -> {
                Log.i(TAG,"recibiendo: $msg")
                val newFlashData = gson.fromJson(msg, FlashData::class.java)
                swList = newFlashData.swList
                saveData(swList)
                mqttManager.unsubscribe(id)
                initializeSwList()
            }
            newSwId -> {
                newSwId = ""
                newSw -= id
                val newEspData = gson.fromJson(msg, EspData::class.java)
                swMap[id] = newEspData
                swList += SwData(newEspData.name, id, (swList.size + 1), "nada", SwStatus.CONNECTED)
                saveData(swList)
                //initializeSwList()
            }
            else -> {
                swMap[id] =gson.fromJson(msg, EspData::class.java)
                if(swMap[id]?.mode == SwMode.TIMERS_TEMP.ordinal){
                    val tempId = (if(id.substring(0,2).toInt(16) == 255)
                        254 else id.substring(0,2).toInt(16) + 1).toString(16) + id.substring(2,id.length)
                    if (swList.indexOfFirst { it.id == tempId } == -1) {
                        setSwWithId(tempId)
                    }
                }
                swList [swList.indexOfFirst { it.id == id }].status = SwStatus.CONNECTED
            }
        }
        Log.i(TAG, "Rx msg: $msg")
        refreshScreenInfo()
    }

    override fun notifyMqttState(mqttState: MqttState) {
        if (mqttState == MqttState.UP) {
            mqttUp = true
            initializeSwList()
        }
    }

    override fun notifySubscribed(id: String) {
        if( id != allSwId){
            initSw(id)
        }
    }
    override fun NotifyApData(myAp: ApData) {
        coroutineScope.launch { myApData.emit(myAp) }
        currentSsid = myAp.ssid
        currentBssid = myAp.bssid
    }
    override fun NotifyTouch(id: String, state: TouchState) {
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
        if (swList.indexOfFirst { it.id == id } == -1) { // it is a new id
            newSwId = id
            newSw += id
            if (mqttUp) {
                mqttManager.subscribe(id)
            }
        }
    }

    fun imageClick(id: String) {
        if (swList [swList.indexOfFirst { it.id == id }].status == SwStatus.CONNECTED) {
            swList [swList.indexOfFirst { it.id == id }].status = SwStatus.CONNECTING
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

    fun configUpgrade(newData: ConfigurableData, id:String){
        if (newData.name != swMap[id]!!.name ||
            newData.prgs != swMap[id]!!.prgs ||
            newData.mode != swMap[id]!!.mode ||
            newData.secs != swMap[id]!!.secs){
            val setData = Global.gson.toJson( EspData(
                newData.name,
                SwState.SET_DATA.ordinal,
                newData.mode,
                newData.secs,
                newData.prgs,
                0))
            mqttManager.publish(id,setData)
        }
        val index = swList.indexOfFirst { it.id == id }
        val data = swList[index]
        var aChange = false
        if(newData.name != data.name){
            data.name = newData.name
            aChange = true
        }
        if(newData.bkColor != data.bkColor){
            data.bkColor = newData.bkColor
            aChange = true
        }
        if(newData.row != data.row){
            swList.remove(data)
            swList.add(newData.row - 1, data)
            swList.forEachIndexed { i, _  ->
                swList[i].row = i + 1
            }
            aChange = true
        }
        if(aChange){
            refreshScreenInfo()
            saveData(swList)
        }
    }

    fun sendConfig(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getFlashData.collect { flashData ->
                Log.i(TAG,"enviando: $flashData")
                mqttManager.publish(id, flashData)
            }
        }
    }
    fun discoverSwitches(pass: String){
        espTouch.discover(currentSsid, currentBssid, pass)
    }

    private fun refreshScreenInfo() {
        val refreshList: MutableList<SwScreenData> = mutableListOf()
        for (swData in swList){
            refreshList += SwScreenData(
                name = swData.name,
                id = swData.id,
                row = swData.row,
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
            SwMode.TEMP.ordinal -> return "Enciende si temp < ${swMap[id]!!.secs / 10}°"
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
        }
        checkSwitches()
    }

    private fun getStoredData() {
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.getFlashData.collect { flashData ->
                val gson = Gson()
                val stored = gson.fromJson(flashData, FlashData::class.java)
                if (stored != null){
                    swList = stored.swList
                }
                if (swList.size == 0) {
                    coroutineScope.launch {starter.emit(true) }
                }
                refreshScreenInfo()
            }
        }
    }

    private fun saveData(list: MutableList<SwData>) {
        for(swData in list){swData.status = SwStatus.DISCONNECTED}
        val gson = Gson()
        val flashData = gson.toJson(FlashData(FLASH_VERSION, list))
        CoroutineScope(Dispatchers.IO).launch {
            swDataStore.saveFlashData(flashData)
        }
    }

    private fun checkSwitches() {
        val timerInSec = 10L
        val initTimerInSec = 5L
        fixedRateTimer("timer", false, initTimerInSec * 1000, timerInSec * 1000) {
            for (sw in swList) {
                if (sw.status == SwStatus.DISCONNECTED) {
                    initSw(sw.id)
                }
            }
            for (id in newSw){
                initSw(id)
            }
        }
    }

    private fun initSw(id: String) {
        mqttManager.publish(id, SEND_GET)
    }

    fun upgrade(server: String, port: String) {
        Log.i(TAG,"server: $server, port: $port")
    }
    fun localErase(id: String){
        swList.removeIf{it.id == id}
        swList.forEachIndexed { index, _  ->
            swList[index].row = index + 1
        }
        saveData(swList)
        refreshScreenInfo()
    }
    fun fullErase(id:String){
        localErase(id)
        mqttManager.publish(id, SEND_ERASE)
        mqttManager.unsubscribe(id)
    }

    fun receiveConfig() {
        if (mqttUp) {
            mqttManager.subscribeFromPhone(allSwId)
        }
    }
}