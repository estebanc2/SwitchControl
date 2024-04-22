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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val keepSwData: KeepSwData
) : ViewModel() {
    var swScreenList by mutableStateOf<List<SwScreenData>>(listOf())
        private set
    var showAdd by mutableStateOf<Boolean> (false)
        private set
    var goConfig by mutableStateOf<Boolean> (false)
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

    fun onConfig(show: Boolean, item: SwScreenData) {
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

    fun onShowAdd(show: Boolean) {
        showAdd = show
    }

}
