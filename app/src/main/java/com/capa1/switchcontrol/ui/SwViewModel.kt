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
import com.capa1.switchcontrol.data.model.ConfigurableData
import com.capa1.switchcontrol.data.model.KeepSwData
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.WeeklyProgram
import com.capa1.switchcontrol.data.wifi.ApData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val keepSwData: KeepSwData
) : ViewModel() {
    var id = ""
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
    val allSwId = keepSwData.allSwId
    var myAp by mutableStateOf (ApData("", false))
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
    var configurableData by mutableStateOf (ConfigurableData("", 0, 0, NO_TIMERS, "nada", 2))
        private set

    fun start(){
        keepSwData.initOperation()
        subscribeToChanges()
    }
    private fun subscribeToChanges() {
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
    }
    fun imageClick(id: String){
        keepSwData.imageClick(id)
    }
    fun saveConfig(){
        keepSwData.configUpgrade(configurableData, id)
        goConfig = false
    }
    fun exitConfig(){
        goConfig = false
    }
    fun onConfig(show: Boolean, item: SwScreenData) {
        id = item.id
        swState = item.swImageId != R.drawable.no_info
        configurableData = ConfigurableData(
            item.name,
            keepSwData.swMap[item.id]?.mode ?: 0,
            keepSwData.swMap[item.id]?.secs ?: 0,
            (keepSwData.swMap[item.id]?.prgs ?: NO_TIMERS).toMutableList(),
            item.bkColor,
            item.row
        )
        goConfig = show
    }
    fun newName(name: String){
        configurableData.name = name
        showName = false
    }
    fun newColor(color: String){
        configurableData.bkColor = color
    }
    fun changeRow(pos: Int) {
        configurableData.row += pos
        Log.i(TAG,"row: [${configurableData.row}]")
    }
    fun newTimer(newPrg: WeeklyProgram){
        configurableData.prgs[currentTimer] = newPrg
        showTimer = false
    }
    fun setMode(mode: Int, secs: Int) {
        configurableData.mode = mode
        configurableData.secs = secs
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
        keepSwData.sendConfig(id)
    }
    fun onShowMaintenance(show: Boolean) {
        showMaintenance = show
    }
}
