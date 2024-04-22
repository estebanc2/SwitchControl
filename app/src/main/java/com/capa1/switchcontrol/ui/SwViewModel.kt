package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.data.Global.TAG
import com.capa1.switchcontrol.data.model.KeepSwData
import com.capa1.switchcontrol.data.model.SwData
import com.capa1.switchcontrol.data.model.SwScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val keepSwData: KeepSwData
) : ViewModel() {
    var swList by mutableStateOf<List<SwData>>(listOf())
        private set
    var swScreenMap by mutableStateOf<Map<String, SwScreenData>>(mapOf())
        private set
    private val _screenModifiers = MutableStateFlow(ScreenModifiers())
    val screenModifiers: StateFlow<ScreenModifiers> = _screenModifiers

    fun start(){
        keepSwData.initOperation()
        subscribeToChanges()
    }

    private fun subscribeToChanges() {
        viewModelScope.launch {
            keepSwData.swList.collect { result ->
                Log.i(TAG, "swList changed!!")
                swList = result
                _screenModifiers.update { currentState ->
                    currentState.copy(swList = result)
                }
            }
        }
        viewModelScope.launch {
            keepSwData.swMap.collect() { result ->
                Log.i(TAG, "swMap changed!!")
                _screenModifiers.update { currentState ->
                    currentState.copy(swMap = result)
                }
            }
        }
        viewModelScope.launch {
            keepSwData.swScreenMap.collect() { result ->
                Log.i(TAG, "swSceenMap changed!!")
                swScreenMap = result
                _screenModifiers.update { currentState ->
                    currentState.copy(swScreenMap = result)
                }
            }
        }
        viewModelScope.launch {
            keepSwData.counter.collect() { result ->
                Log.i(TAG, "counter changed!!: new value = $result")
                }
        }
    }
    fun imageClick(id: String){
        keepSwData.imageClick(id)
    }
    fun changeName (name: String){

    }

    fun exit() {
        TODO("Not yet implemented")
    }

    fun process() {
        TODO("Not yet implemented")
    }

    fun picker() {
        TODO("Not yet implemented")
    }

    fun showAdd(show: Boolean) {
        _screenModifiers.update { currentState ->
            currentState.copy(showAdd = show)
        }
    }

    fun newId() {
        TODO("Not yet implemented")
    }
}
