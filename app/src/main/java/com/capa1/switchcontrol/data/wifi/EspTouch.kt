package com.capa1.switchcontrol.data.wifi

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.util.Log
import com.capa1.switchcontrol.data.Global.TAG

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EspTouch @Inject constructor(private val context: Context) {

    private var wifiPass= ""


    private fun wifiSwConfig(mySsid: String, myBssid: String, myPass: String) {
        //val task = EsptouchTask(mySsid, myBssid, myPass, context)
        val waitInSec = 2
        val timeOut = ScheduledThreadPoolExecutor(1)
        timeOut.schedule({
            Log.i(TAG, "timeout ")
            //task.interrupt()
            wifiFail()
        }, waitInSec.toLong(), TimeUnit.SECONDS)
        //task.setPackageBroadcast(false) // if true send broadcast packets, else send multicast packets
        //task.setEsptouchListener { result ->
        //    val newId: String = result.bssid
        //    Log.i(TAG, "newId: [$newId]")
         //   timeOut.isTerminated    //remove()  .cancel()
        //}
     }

    private fun wifiFail() {
        //wifiCredentialConfig()
    }

}