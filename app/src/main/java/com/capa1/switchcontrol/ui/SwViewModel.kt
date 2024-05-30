package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.Global.NO_TIMERS
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.KeepSwData
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwMode
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.data.model.SwStatus
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.data.wifi.ApData
import com.capa1.switchcontrol.data.wifi.TouchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val keepSwData: KeepSwData
) : ViewModel() {
    var id = ""
    var server = ""
    var port = ""
    var swState = false
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
    var upgrading by mutableStateOf (0)
        private set
    val allSwId = keepSwData.allSwId
    var myAp by mutableStateOf (ApData(""," ",false))
        private set
    var swScreenList by mutableStateOf<List<SwScreenData>>(listOf())
        private set
    var currentTimer by mutableStateOf(0)
        private set
    var showMode by mutableStateOf (false)
        private set
    var showMaintenance by mutableStateOf (false)
        private set
    var goConfig by mutableStateOf (false)
        private set
    var touchProgress by mutableStateOf (TouchState.IN_PROGRESS)
        private set
    private var name = ""
    private var bkColor = "nada"
    private var row = 1
    private var prg = NO_TIMERS
    private var mode = SwMode.PULSE_NA
    private var secs = 0


    var localSwData:SwData = SwData("", SwState.OFF, SwMode.TIMERS, 0,
        NO_TIMERS, "nada", 2, SwStatus.DISCONNECTED, 0)


    fun start(){
        keepSwData.initOperation()
        subscribeToChanges()
    }
    private fun subscribeToChanges() {
        viewModelScope.launch {
            keepSwData.upgradeState.collect { result ->
                upgrading = result
            }
        }
        viewModelScope.launch {
            keepSwData.swScreenList.collect { result ->
                swScreenList = result
            }
        }
        viewModelScope.launch {
            keepSwData.myApData.collect { result ->
                myAp = result
            }
        }
        viewModelScope.launch {
            keepSwData.touchState.collect { result ->
                touchProgress = result
                if (result == TouchState.READY){
                    showAdd = false
                    showNew = false
                }
            }
        }
        viewModelScope.launch {
            keepSwData.starter.collect { result ->
                showAdd = result
            }
        }
    }
    fun imageClick(id: String){
        /* // con esto se pone el dibujito de la transiscion pero no funciona. revisar si hay recomposicion
        if (swScreenList [swScreenList.indexOfFirst { it.id == id }].swImageId == R.drawable.close) {
            swScreenList [swScreenList.indexOfFirst { it.id == id }].swImageId = R.drawable.opening
        }
        if (swScreenList [swScreenList.indexOfFirst { it.id == id }].swImageId == R.drawable.open) {
            swScreenList [swScreenList.indexOfFirst { it.id == id }].swImageId = R.drawable.closing
        }
        */
        keepSwData.imageClick(id)
    }
    fun saveConfig(){
        //keepSwData.configUpgrade(localSwData, id)
        Log.i(TAG,"3 (saveConfig) vm: ${localSwData.bkColor} ks: ${keepSwData.getCurrentSwData(id).bkColor}")
        goConfig = false
    }
    fun exitConfig(){
        goConfig = false
    }
    fun newName(name: String){
        localSwData.name = name
        showName = false
    }
    fun newColor(color: String){
        Log.i(TAG,"2 (newColor) vm: de ${localSwData.bkColor} a $color, ks: ${keepSwData.getCurrentSwData(id).bkColor}")
        localSwData.bkColor = color
    }
    fun changeRow(pos: Int) {
        localSwData.row += pos
        Log.i(TAG,"row: [${localSwData.row}]")
    }
    fun newTimer(newPrg: WeeklyProgram){
        localSwData.prgs[currentTimer] = newPrg
        showTimer = false
    }
    fun setMode(mode: SwMode, secs: Int) {
        localSwData.mode = mode
        localSwData.secs = secs
        showMode = false
    }
    fun setPass(pass: String) {
        keepSwData.discoverSwitches(pass)
    }
    fun addSwId (id: String) {
        keepSwData.setSwWithId(id)
        showNewId = false
        showAdd = false
    }
    fun onConfig(show: Boolean, item: SwScreenData) {
        id = item.id
        swState = item.swImageId != R.drawable.no_info
        localSwData = keepSwData.getCurrentSwData(id)
        Log.i(TAG,"1 (onConfig) vm: ${localSwData.bkColor}, ks: ${keepSwData.getCurrentSwData(id).bkColor}")
        goConfig = show
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
    fun addAllSw(id: String) {
        if(id != "0"){
            keepSwData.sendConfigToOtherPhone(id)
        }
        else{
            keepSwData.receiveConfigFromOtherPhone()
        }
        showAdd = false
        showAll = false
    }
    fun onShowMaintenance(show: Boolean) {
        showMaintenance = show
    }
    fun upgrade(server: String, port: String) {
        this.server = server
        this.port = port
        keepSwData.firmwareUpgrade(id, server, port)
    }
    fun localErase(){
        keepSwData.localErase(id)
        showMaintenance = false
        exitConfig()
    }
    fun fullErase(){
        keepSwData.fullErase(id)
        showMaintenance = false
        exitConfig()
    }
}
