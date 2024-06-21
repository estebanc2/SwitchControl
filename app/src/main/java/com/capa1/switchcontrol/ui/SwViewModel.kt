package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.Global.MyColors
import com.capa1.switchcontrol.data.Global.NO_TIMERS
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.LegendMaker
import com.capa1.switchcontrol.data.SwDataStore
import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.StoredData
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwMode
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.data.model.ToStore
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.data.mqtt.MqttManager
import com.capa1.switchcontrol.data.mqtt.MqttState
import com.capa1.switchcontrol.data.wifi.ApData
import com.capa1.switchcontrol.data.wifi.EspTouch
import com.capa1.switchcontrol.data.wifi.TouchState
import com.capa1.switchcontrol.data.wifi.WifiCredentials
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val mqttManager: MqttManager,
    private val wifiCredentials: WifiCredentials,
    private val swDataStore: SwDataStore,
    private val espTouch: EspTouch,
    private val legendMaker: LegendMaker
) : ViewModel() {

    private val swMap = mutableMapOf<String, SwData>()
    val allSwId = Random.nextInt(9).toString() + "0123456789"+ Random.nextInt(9).toString()
    private val newSw = mutableListOf<String>()
    private var newSwId = ""
    private var mqttUp = false
    private var upgradingId = ""
    private var started = false
    var currentId = ""
    var server = ""
    var port = ""
    var showName by mutableStateOf (false)
        private set
    var showColor by mutableStateOf (false)
        private set
    var showTimer by mutableStateOf (false)
        private set
    var showAdd by mutableStateOf (false)
        private set
    var showNewId by mutableStateOf (false)
        private set
    var showNew by mutableStateOf (false)
        private set
    var showAll by mutableStateOf (false)
        private set
    var upgrading by mutableIntStateOf (0)
        private set
    var myAp by mutableStateOf (ApData(""," ",false))
        private set
    var swScreenList by mutableStateOf<List<SwScreenData>>(listOf())
        private set
    var currentTimer by mutableIntStateOf(0)
        private set
    var showMode by mutableStateOf (false)
        private set
    var showMaintenance by mutableStateOf (false)
        private set
    var showConfig by mutableStateOf (false)
        private set
    var touchProgress by mutableStateOf (TouchState.IN_PROGRESS)
        private set
    var currentSwData by mutableStateOf (SwData("", SwState.OFF, SwMode.TIMERS, 0,
        NO_TIMERS, 0, "nothing", 2, SwStatus.DISCONNECTED))
        private set

    fun start(){
        Log.i(TAG," IN START")
        if(!started){
            getStoredData()
            mqttManager.connect()
            subscribeToChanges()
            wifiCredentials.get()
        }
    }
    private fun subscribeToChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            mqttManager.mqttState.collect { result ->
                if (result == MqttState.UP) {
                    mqttUp = true
                    initializeSw()
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            mqttManager.arrival.collect { result ->
                processArrival(result. first, result.second)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            mqttManager.subscribedId.collect { result ->
                if (result != allSwId && result != ""){
                    initSw(result)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            espTouch.touched.collect { result ->
                touchProgress = result.second
                if(touchProgress == TouchState.READY){
                    Log.i(TAG, "esptouch gave mac: $result.first")
                    setSwWithId(result.first)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            wifiCredentials.apData.collect { result ->
                myAp = result
            }
        }
    }

    private fun processArrival(id:String, msg: String){
        if (id == "" || msg == ""){
            return
        }
        val gson = Gson()
        when (id) {
            allSwId -> {
                val received = gson.fromJson(msg, ToStore::class.java)
                if (received != null) {
                    received.list.forEachIndexed { i, data ->
                        swMap[data.id] = SwData(
                            name = data.name,
                            state = SwState.OFF,
                            mode = SwMode.TIMERS,
                            secs = 0,
                            prgs = NO_TIMERS,
                            tempX10 = 0,
                            bkColor = data.bkColor,
                            row = i + 1,
                            status = SwStatus.DISCONNECTED
                        )
                    }
                    saveData()
                    initializeSw()
                } else {
                    Log.i(TAG, "bad received data!")
                }
            }

            newSwId -> {
                newSwId = ""
                newSw -= id
                val esp = gson.fromJson(msg, EspData::class.java)
                if (esp != null) {
                    swMap[id] = SwData(
                        name = esp.name,
                        state = SwState.entries[esp.state],
                        mode = SwMode.entries[esp.mode],
                        secs = esp.secs,
                        prgs = esp.prgs,
                        tempX10 = esp.tempX10,
                        bkColor = "nothing",
                        row = swMap.size + 1,
                        status = SwStatus.CONNECTED
                    )
                    saveData()
                }
            }

            upgradingId -> {
                upgrading = gson.fromJson(msg, EspData::class.java).state
                if (upgrading == SwState.UPGRADED.ordinal) {
                    upgradingId = ""
                }
            }

            else -> {
                val esp = gson.fromJson(msg, EspData::class.java)
                if (swMap[id] != null) {
                    swMap[id] = SwData(
                        esp.name,
                        SwState.entries[esp.state],
                        SwMode.entries[esp.mode],
                        esp.secs,
                        esp.prgs,
                        esp.tempX10,
                        swMap[id]?.bkColor ?: "nada",
                        swMap[id]?.row ?: 1,
                        SwStatus.CONNECTED
                    )
                    if (swMap[id]?.mode == SwMode.TIMERS_TEMP) {
                        val tempId = (if (id.substring(0, 2).toInt(16) == 255)
                            254 else id.substring(0, 2)
                            .toInt(16) + 1).toString(16) + id.substring(2, id.length)
                        if (!swMap.contains(tempId)) {
                            setSwWithId(tempId)
                        }
                    }
                }
            }
        }
        Log.i(TAG, "Rx: $id -> $msg")
        refreshScreenInfo()
    }

    private fun getStoredData() {
        viewModelScope.launch(Dispatchers.IO) {
            swDataStore.getFlashData().collect { flashData ->
                Log.i(TAG,"get from memory: $flashData")
                if (!started) {
                    started = true
                    val gson = Gson()
                    val stored = gson.fromJson(flashData, ToStore::class.java)
                    if (stored != null) {
                        if (stored.list.size == 0) {
                            showAdd = true
                        } else {
                            stored.list.forEachIndexed { i, data ->
                                var color = data.bkColor
                                if (!MyColors.containsKey(color)) color = "nothing"
                                swMap[data.id] = SwData(
                                    name = data.name,
                                    state = SwState.OFF,
                                    mode = SwMode.TIMERS,
                                    secs = 0,
                                    prgs = NO_TIMERS,
                                    tempX10 = 0,
                                    bkColor = color,
                                    row = i + 1,
                                    status = SwStatus.DISCONNECTED
                                )
                            }
                            refreshScreenInfo()
                        }
                    } else {
                        Log.i(TAG, "bad stored data!")
                    }
                }
            }
        }
    }
    private fun saveData() {
        val list = mutableListOf<StoredData>()
        swMap.toList().sortedBy { it.second.row }. forEach { (id, swData) ->
            list.add(StoredData(swData.name, id, swData.bkColor))
        }
        val gson = Gson()
        val toStore = gson.toJson(ToStore(list))
        viewModelScope.launch(Dispatchers.IO){
            swDataStore.saveFlashData(toStore)
        }
    }
    private fun refreshScreenInfo() {
        val list = mutableListOf<SwScreenData>()
        swMap.toList().sortedBy { it.second.row }.forEach { (id, swData) ->
            list.add(SwScreenData(  name = swData.name,
                                            id = id,
                                            row = swData.row,
                                            bkColor = swData.bkColor,
                                            swImageId = getSwImageId(id),
                                            timerInfo = legendMaker.getLegend(swData)
                                        ))
        }
        Log.i(TAG,"swScreenList refresh")
        swScreenList = list
    }

    private fun getSwImageId(id: String): Int {
        if (!swMap.containsKey(id) || swMap.getValue(id).status == SwStatus.DISCONNECTED) {
            return R.drawable.no_info
        }
        if (swMap.getValue(id).status == SwStatus.CONNECTING){
            if (swMap.getValue(id).state == SwState.OFF) {
                return R.drawable.opening
            }
            if (swMap.getValue(id).state == SwState.ON) {
                return R.drawable.closing
            }
        }

        when (swMap.getValue(id).mode to swMap.getValue(id).state) {
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
    private fun checkSwitches() {
        val timerInSec = 1L
        fixedRateTimer("timer", false, timerInSec * 1000, timerInSec * 1000) {
            swMap.forEach { (id, swData)->
                if (swData.status == SwStatus.DISCONNECTED) {
                    initSw(id)
                    Log.i(TAG,"id no connected: ${id})")
                }
            }
            for (id in newSw){
                initSw(id)
            }
        }
    }
    private fun initSw(id: String) {
        mqttManager.publish(id, Global.SEND_GET)
    }
    fun setSwWithId(id: String) {
        if (!swMap.contains(id)) {
            newSwId = id
            newSw += id
            if (mqttUp) {
                mqttManager.subscribe(id)
            }
        }
        showNewId = false
        showAdd = false
    }
    fun imageClick(id: String){
        if (swMap[id]?.status == SwStatus.CONNECTED) {
            swMap [id]?.status = SwStatus.CONNECTING
            when ( swMap[id]?.state ) {
                SwState.OFF -> {
                    mqttManager.publish(id, Global.SEND_ON)
                }
                SwState.ON -> {
                    mqttManager.publish(id, Global.SEND_OFF)
                }
                else -> {
                    swMap [id]?.status = SwStatus.CONNECTED
                }
            }
        }
    }
    fun saveConfig(){
        if (swMap.getValue(currentId).name != currentSwData.name ||
            swMap.getValue(currentId).prgs != currentSwData.prgs ||
            swMap.getValue(currentId).mode != currentSwData.mode ||
            swMap.getValue(currentId).secs != currentSwData.secs){
            val setData = Global.gson.toJson(EspData (  currentSwData.name,
                SwState.SET_DATA.ordinal,
                currentSwData.mode.ordinal,
                currentSwData.secs,
                currentSwData.prgs,
                currentSwData.tempX10 ))
            mqttManager.publish(currentId,setData)
        }
        if (swMap.getValue(currentId).row > currentSwData.row) {
            swMap.forEach { (id, swData) ->
                if (swData.row < swMap.getValue(currentId).row && swData.row >= currentSwData.row) {
                    swMap[id]?.row  =  swData.row + 1
                }
            }
        }
        if(swMap.getValue(currentId).row < currentSwData.row) {
            swMap.forEach { (id, swData) ->
                if (swData.row > swMap.getValue(currentId).row && swData.row <= currentSwData.row) {
                    swMap[id]?.row = swData.row - 1
                }
            }
        }
        if (swMap.getValue(currentId).name != currentSwData.name ||
            swMap.getValue(currentId).bkColor != currentSwData.bkColor ||
            swMap.getValue(currentId).row != currentSwData.row ){
            swMap[currentId] = currentSwData.copy()
            refreshScreenInfo()
            saveData()
        }
        showConfig = false
    }
    fun exitConfig(){
        showConfig = false
    }
    fun goConfig(item: SwScreenData) {
        currentId = item.id
        currentSwData = swMap.getValue(currentId).copy()
        currentSwData.prgs = swMap.getValue(currentId).prgs.toMutableList()
        for (i in 0..< currentSwData.prgs.size) {
            currentSwData.prgs[i] = (swMap.getValue(currentId).prgs[i]).copy()
        }
        showConfig = true
    }
    fun newName(name: String){
        currentSwData.name = name
        showName = false
    }
    fun newColor(color: String){
         currentSwData.bkColor = color
    }
    fun changeRow(pos: Int) {
        currentSwData.row += pos
    }
    fun newTimer(newPrg: WeeklyProgram){
        currentSwData.prgs[currentTimer] = newPrg.copy()
        showTimer = false
    }
    fun setMode(mode: SwMode, secs: Int) {
        currentSwData.mode = mode
        currentSwData.secs = secs
        showMode = false
    }
    fun discoverSwitches(pass: String) {
        espTouch.discover(myAp.ssid, myAp.bssid, pass)
    }
    fun onShowAdd(show: Boolean) {
        showAdd = show
    }
    fun onShowName(show: Boolean) {
        showName = show
    }
    fun onShowColor(show: Boolean) {
        showColor = show
    }
    fun onShowTimer(timer: Int, show: Boolean) {
        if (show){
            currentTimer = timer
        }
        showTimer = show
    }
    fun onShowMode(show: Boolean) {
        showMode = show
    }
    fun onShowNewId(show: Boolean) {
        showNewId = show
    }
    fun onShowNew(show: Boolean) {
        showNew = show
    }
    fun onShowAll(show: Boolean) {
        showAll = show
    }
    fun onShowMaintenance(show: Boolean) {
        showMaintenance = show
    }
    fun firmwareUpgrade(server: String, port: String) {
        this.server = server
        this.port = port
        upgradingId = currentId
        val setData = Global.gson.toJson( EspData(
            name = server,
            state = SwState.UPGRADE.ordinal,
            mode = 0,
            secs = port.toInt(),
            prgs = NO_TIMERS,
            tempX10 = 0
        ))
        mqttManager.publish(currentId,setData)
    }
    fun addAllSw(id: String) {
        if(id != "0"){
            viewModelScope.launch(Dispatchers.IO) {
                swDataStore.getFlashData().collect { flashData ->
                    mqttManager.publish(id, flashData)
                }
            }
        }
        else{
            if (mqttUp) {
                mqttManager.subscribeFromPhone(allSwId)
            }
        }
        showAdd = false
        showAll = false
    }
    fun localErase(){
        val erasedRow = swMap.getValue(currentId).row
        swMap.remove(currentId)
        swMap.forEach { (key, value) ->
            if (value.row > erasedRow) {
                swMap.getValue(key).row = value.row - 1
            }
        }
        saveData()
        refreshScreenInfo()
        showMaintenance = false
        exitConfig()
    }
    fun fullErase(){
        localErase()
        mqttManager.publish(currentId, Global.SEND_ERASE)
        mqttManager.unsubscribe(currentId)
    }
}
