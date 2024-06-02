package com.capa1.switchcontrol.data.wifi

import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.util.Log
import com.capa1.switchcontrol.data.Global.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WifiCredentials @Inject constructor(
    private val context: Context,
) {
    val apData: MutableStateFlow<ApData> = MutableStateFlow(ApData("unknown", "", false))
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun get() {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE)!! as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                val str = wifiInfo.ssid
                coroutineScope.launch{apData.emit(ApData(str.substring(1, str.length - 1), wifiInfo.bssid, wifiInfo.frequency > 3000))}
             }
            else{
                coroutineScope.launch{apData.emit(ApData("unknown", "", false))}
            }
        } catch (a: Throwable) {
            Log.w(TAG, "", a)
        }
    }
}