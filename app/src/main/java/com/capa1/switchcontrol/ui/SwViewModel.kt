package com.capa1.switchcontrol.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capa1.switchcontrol.data.Controller
import com.capa1.switchcontrol.data.model.SwData
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


    private fun subscribeToChanges() {
        viewModelScope.launch {
            controller.swList.collect { result ->
                _screenModifiers.update { currentState ->
                    currentState.copy(swList = result)
                }
            }
        }
    }
}
