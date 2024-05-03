package com.capa1.switchcontrol.data.wifi

interface WifiListener {
    fun NotifyApData(myAp: ApData)
    fun NotifyId(id: String)
}