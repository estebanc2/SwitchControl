package com.capa1.switchcontrol.data.wifi

interface WifiListener {
    fun NotifyApData(myAp: ApData)
    fun NotifyTouch(id: String, state:TouchState)
}