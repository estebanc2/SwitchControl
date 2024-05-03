package com.capa1.switchcontrol.data.wifi

import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.util.Log
import com.capa1.switchcontrol.data.Global.TAG
import javax.inject.Inject

class WifiCredentials @Inject constructor(
    private val context: Context,
    private val listener: WifiListener
) {
    fun get() {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE)!! as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                listener.NotifyApData(ApData(wifiInfo.ssid, wifiInfo.frequency > 3000))
            }
            else{
                listener.NotifyApData(ApData("uknown", false))
            }
        } catch (a: Throwable) {
            Log.w(TAG, "", a)
        }
    }
}