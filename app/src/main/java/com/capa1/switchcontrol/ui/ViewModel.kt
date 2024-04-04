package com.capa1.switchcontrol.ui

import androidx.lifecycle.ViewModel
import com.capa1.switchcontrol.data.Controller
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModel  @Inject constructor(
    private val controller: Controller
) : ViewModel() {

    private val _screenModifiers = MutableStateFlow(ScreenModifiers())
    val screenModifiers: StateFlow<ScreenModifiers> = _screenModifiers

    fun getCalList(): List<String> {
        return listOf("a", "b")
    }

    fun showList(b: Boolean) {

    }

}
