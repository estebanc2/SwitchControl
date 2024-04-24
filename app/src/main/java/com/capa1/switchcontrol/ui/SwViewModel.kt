package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.data.Global.NO_TIMERS
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.ConfigurableData
import com.capa1.switchcontrol.data.model.KeepSwData
import com.capa1.switchcontrol.data.model.SwScreenData
import com.capa1.switchcontrol.data.model.WeeklyProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val keepSwData: KeepSwData
) : ViewModel() {
    private var lastId = ""
    var showName by mutableStateOf (false)
        private set
    var showColor by mutableStateOf (false)
        private set
    var showTimer by mutableStateOf (false)
        private set
    var showMode by mutableStateOf (false)
        private set
    var showAdd by mutableStateOf (false)
        private set
    var showNewId by mutableStateOf (false)
        private set
    var showNew by mutableStateOf (false)
        private set
    var showAll by mutableStateOf (false)
        private set

    var swScreenList by mutableStateOf<List<SwScreenData>>(listOf())
        private set
    var goConfig by mutableStateOf (false)
        private set
    var configurableData by mutableStateOf<ConfigurableData> (ConfigurableData("", 0,
                                    0, NO_TIMERS, "nada", 2, listOf()))
        private set

    fun start(){
        keepSwData.initOperation()
        subscribeToChanges()
    }
    private fun subscribeToChanges() {
        viewModelScope.launch {
            keepSwData.swScreenList.collect { result ->
                Log.i(TAG, "swScreenList changed!!")
                swScreenList = result
            }
        }
    }
    fun imageClick(id: String){
        keepSwData.imageClick(id)

    }
    fun saveConfig(){
        keepSwData.configUpgrade(configurableData, lastId)
        goConfig = false
    }
    fun exitConfig(){
        goConfig = false
    }
    fun onConfig(show: Boolean, item: SwScreenData) {
        lastId = item.id
        configurableData = ConfigurableData(
            item.name,
            keepSwData.swMap[item.id]?.mode ?: 0,
            keepSwData.swMap[item.id]?.secs ?: 0,
            keepSwData.swMap[item.id]?.prgs ?: NO_TIMERS,
            item.bkColor,
            item.row,
            keepSwData.getTimersInfo(item.id)
        )
        goConfig = show
    }
    fun changeName(name: String){

    }
    fun changeColor(color: String){
        configurableData.bkColor = color
    }
    fun changeRow(pos: Int) {
        configurableData.row += pos
        Log.i(TAG,"row: [${configurableData.row}]")
    }
    fun changeTimer(newPrg: WeeklyProgram){

    }
    fun changeMode(mode: Int){

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
    fun onShowTimer(show: Boolean) {
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
}
