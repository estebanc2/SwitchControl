package com.capa1.switchcontrol.data.wifi

interface WifiListener {
    fun notifyApData(myAp: ApData)
    fun notifyTouch(id: String, state:TouchState)
}