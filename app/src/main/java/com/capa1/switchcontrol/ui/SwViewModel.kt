package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.data.Global
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.LegendMaker
import com.capa1.switchcontrol.data.SwDataStore
import com.capa1.switchcontrol.data.model.DialogState
import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.Mode
import com.capa1.switchcontrol.data.model.NO_TIMERS
import com.capa1.switchcontrol.data.model.SEND_ERASE
import com.capa1.switchcontrol.data.model.SEND_GET
import com.capa1.switchcontrol.data.model.ScreenData
import com.capa1.switchcontrol.data.model.State
import com.capa1.switchcontrol.data.model.StoredData
import com.capa1.switchcontrol.data.model.TEST
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
import kotlin.random.Random

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val mqttManager: MqttManager,
    private val wifiCredentials: WifiCredentials,
    private val swDataStore: SwDataStore,
    private val espTouch: EspTouch,
    private val legendMaker: LegendMaker
) : ViewModel() {
    private val espMap = mutableMapOf<String, EspData>()
    val allSwId = Random.nextInt(9).toString() + "0123456789"+ Random.nextInt(9).toString()
    private val newSw = mutableListOf<String>()
    private var newSwId = ""
    private var mqttUp = false
    private var upgradingId = ""
    var currentId = ""
    var server = ""
    var port = ""
    var currentEspData: EspData = EspData("", State.OFF, Mode.TIMERS, 0, NO_TIMERS,0)
    var dialogState by mutableStateOf(DialogState())
        private set
    var upgrading by mutableIntStateOf (0)
        private set
    var myAp by mutableStateOf (ApData(""," ",false))
        private set
    val swScreenList = mutableStateListOf<ScreenData>()
    var currentTimer by mutableIntStateOf(0)
        private set
    var touchProgress by mutableStateOf (TouchState.IN_PROGRESS)
        private set

    fun start(){
        Log.i(TAG," IN START")
        getStoredData()
        mqttManager.mqttInit()
        subscribeToChanges()
        wifiCredentials.get()
    }

    fun toggle(id: String){

    }

    private fun getStoredData() {
        viewModelScope.launch(Dispatchers.IO) {
            swDataStore.myFlashData.collect { json ->
                createSwScreenList(json)
            }
        }
    }

    private fun createSwScreenList(json: String) {
        if (json.isNotEmpty()) {
            val gson = Gson()
            refresh(gson.fromJson(json, ToStore::class.java))
        } else {
            refresh(TEST)
        }
    }

    private fun refresh(stored: ToStore) {
        swScreenList.clear()
        stored.list.forEach { data ->
            swScreenList.add (ScreenData (
                name = data.name,
                id = data.id,
                icon = data.icon,
                timerInfo = legendMaker.legend(espMap[data.id]),
                swOn = false,
                connected = false )
            )
        }
    }

    private fun subscribeToChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            mqttManager.mqttState.collect { result ->
                when(result){
                    MqttState.CONNECTED -> {
                        mqttUp = true
                        initializeSw()
                    }
                    MqttState.CONNECTING -> mqttUp = false
                    MqttState.DISCONNECTED -> {
                        mqttManager.connect()
                        mqttUp = false
                    }
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            mqttManager.arrival.collect { result ->
                processArrival(result.first, result.second)
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

    private fun initializeSw() {
        swScreenList.forEach {data ->
            mqttManager.subscribe(data.id)
        }
    }

    private fun initSw(id: String) {
        val msg = SEND_GET
        mqttManager.publish(id, msg)
        Log.i(TAG, "init Tx: $id -> $msg")
    }

    private fun processArrival(id:String, msg: String){
        if (id == "" || msg == ""){
            return
        }
        if (id == allSwId) {
            createSwScreenList(msg)
            initializeSw()
        } else {
            val gson = Gson()
            espMap[id] = gson.fromJson(msg, EspData::class.java)
            if (id == upgradingId && espMap[id]?.state == State.UPGRADED) {
                upgradingId = ""
            } else if (id == newSwId){
                newSwId = ""
                newSw -= id
                swScreenList.add (ScreenData(
                    name = espMap[id]?.name ?: "",
                    id = id,
                    icon = "",
                    timerInfo = legendMaker.legend(espMap[id]),
                    swOn = espMap[id]?.state == State.ON,
                    connected = true)
                )
                if (espMap[id]?.mode == Mode.TIMERS_TEMP) {
                    val termoId = (if (id.substring(0, 2).toInt(16) == 255)
                        254 else id.substring(0, 2)
                        .toInt(16) + 1).toString(16) + id.substring(2, id.length)
                    mqttManager.subscribe(termoId)
                }
            } else
            swScreenList.find { it.id == id }?.let {sw ->
               sw.name = espMap[id]?.name ?: ""
               sw.timerInfo = legendMaker.legend(espMap[id])
               sw.swOn = espMap[id]?.state == State.ON
               sw.connected = true
            }
        }
    }

    private fun saveData() {
        val list = mutableListOf<StoredData>()
        swScreenList.toList().forEach { sw ->
            list.add(StoredData(sw.name, sw.id, sw.icon))
        }
        val gson = Gson()
        val toStore = gson.toJson(ToStore(list))
        viewModelScope.launch(Dispatchers.IO){
            swDataStore.saveFlashData(toStore)
        }
    }

    fun setSwWithId(id: String) {
        if (!espMap.contains(id)) {
            newSwId = id
            newSw += id
            if (mqttUp) {
                mqttManager.subscribe(id)
            }
        }
        dialogState = dialogState.copy(showNewId = false)
        dialogState = dialogState.copy(showAdd = false)
    }

    fun saveConfig(){
        /*if (espMap.getValue(currentId).name != currentSwData.name ||
            swMap.getValue(currentId).prgs != currentSwData.prgs ||
            swMap.getValue(currentId).mode != currentSwData.mode ||
            swMap.getValue(currentId).secs != currentSwData.secs){
            val setData = Global.gson.toJson(EspData (  currentSwData.name,
                State.SET_DATA,
                currentSwData.mode,
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
            swMap.getValue(currentId).icon != currentSwData.icon ||
            swMap.getValue(currentId).row != currentSwData.row ){
            swMap[currentId] = currentSwData.copy()
            refreshScreenInfo()
            saveData()
        }*/
        dialogState = dialogState.copy(showConfig = false)
    }

    fun exitConfig(){
        dialogState = dialogState.copy(showConfig = false)
    }

    fun goConfig(item: ScreenData) {
        currentId = item.id
        currentEspData = espMap.getValue(currentId).copy()
        currentEspData.prgs = espMap.getValue(currentId).prgs.toMutableList()
        for (i in 0..< currentEspData.prgs.size) {
            currentEspData.prgs[i] = (espMap.getValue(currentId).prgs[i]).copy()
        }
        dialogState = dialogState.copy(showConfig = true)
    }

    fun newName(name: String){
        currentEspData.name = name
        dialogState = dialogState.copy(showName = false)
    }

    fun changeRow(pos: Int) {
        //currentSwData.row += pos
    }

    fun newTimer(newPrg: WeeklyProgram){
        currentEspData.prgs[currentTimer] = newPrg.copy()
        dialogState = dialogState.copy(showTimer = false)
    }
    fun setMode(mode: Mode, secs: Int) {
        currentEspData.mode = mode
        currentEspData.secs = secs
        dialogState = dialogState.copy(showMode = false)
    }
    fun discoverSwitches(pass: String) {
        espTouch.discover(myAp.ssid, myAp.bssid, pass)
    }
    fun onShowAdd(show: Boolean) {
        dialogState = dialogState.copy(showAdd = show)
    }
    fun onShowName(show: Boolean) {
        dialogState = dialogState.copy(showName = show)
    }

    fun onShowTimer(timer: Int, show: Boolean) {
        if (show){
            currentTimer = timer
        }
        dialogState = dialogState.copy(showTimer = show)
    }
    fun onShowMode(show: Boolean) {
        dialogState = dialogState.copy(showMode = show)
    }

    fun onShowIcon (show: Boolean) {
        dialogState = dialogState.copy(showIcon = show)
    }

    fun onShowNewId(show: Boolean) {
        dialogState = dialogState.copy(showNewId = show)
    }
    fun onShowNew(show: Boolean) {
        dialogState = dialogState.copy(showNew = show)
    }
    fun onShowAll(show: Boolean) {
        dialogState = dialogState.copy(showAll = show)
    }

    fun onShowMaintenance(show: Boolean) {
        dialogState = dialogState.copy(showMaintenance = show)
    }

    fun firmwareUpgrade(server: String, port: String) {
        this.server = server
        this.port = port
        upgradingId = currentId
        val setData = Global.gson.toJson( EspData(
            name = server,
            state = State.UPGRADE,
            mode = Mode.TIMERS,
            secs = port.toInt(),
            prgs = NO_TIMERS,
            tempX10 = 0
        ))
        mqttManager.publish(currentId,setData)
    }
    fun addAllSw(id: String) {
        if(id != "0"){
            viewModelScope.launch(Dispatchers.IO) {
                swDataStore.myFlashData.collect { flashData ->
                    mqttManager.publish(id, flashData)
                }
            }
        }
        else{
            if (mqttUp) {
                mqttManager.subscribeFromPhone(allSwId)
            }
        }
        dialogState = dialogState.copy(showAdd = false)
        dialogState = dialogState.copy(showAll = false)
    }
    fun localErase(){
        swScreenList.removeIf{ it.id == currentId }
        dialogState = dialogState.copy(showMaintenance = false)
        exitConfig()
    }
    fun fullErase(){
        localErase()
        mqttManager.publish(currentId, SEND_ERASE)
        mqttManager.unsubscribe(currentId)
    }
}
