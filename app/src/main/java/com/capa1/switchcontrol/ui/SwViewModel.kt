package com.capa1.switchcontrol.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.data.Controller
import com.capa1.switchcontrol.data.Global.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwViewModel  @Inject constructor(
    private val controller: Controller
) : ViewModel() {
    private val _screenModifiers = MutableStateFlow(ScreenModifiers())
    val screenModifiers: StateFlow<ScreenModifiers> = _screenModifiers

    fun start(){
        controller.initOperation()
        subscribeToChanges()
    }

    private fun subscribeToChanges() {
        viewModelScope.launch {
            controller.swList.collect { result ->
                _screenModifiers.update { currentState ->
                    currentState.copy(swList = result)
                }
            }
        }
        viewModelScope.launch {
            controller.swMap.collect { result ->
                _screenModifiers.update { currentState ->
                    currentState.copy(swMap = result)
                }
            }
        }
        viewModelScope.launch {
            controller.swScreenMap.collect { result ->
                _screenModifiers.update { currentState ->
                    currentState.copy(swScreenMap = result)
                }
            }
        }

    }
    fun imageClick(id: String){
        Log.i(TAG,"recivo desde la ui el id = $id")
        controller.imageClick(id)
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
}
