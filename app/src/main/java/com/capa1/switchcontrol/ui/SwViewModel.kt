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
    var swScreenList by mutableStateOf<List<SwScreenData>>(listOf())
        private set
    var showAdd by mutableStateOf<Boolean> (false)
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

    fun onShowAdd(show: Boolean) {
        showAdd = show
    }

    fun newId() {
        TODO("Not yet implemented")
    }
}
