package com.capa1.switchcontrol.data.wifi

import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.util.Log
import com.capa1.switchcontrol.data.Global.TAG
import javax.inject.Inject

class WifiCredentials @Inject constructor(private val context: Context) {
    var mSsid = "no conectado"
    var mBssid = ""
    var is5G = false

    init {
        wifiCredentialConfig()
    }
    private fun wifiCredentialConfig() {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE)!! as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                val str = wifiInfo.ssid
                mSsid = str.substring(1, str.length - 1)
                mBssid = wifiInfo.bssid
                if (wifiInfo.frequency > 3000) {
                    is5G = true
                }
            }
            Log.i(TAG, "ssid = [$mSsid], is5G: $is5G" )
        } catch (a: Throwable) {
            Log.w(TAG, "", a)
        }
    }
}