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

class EspTouch @Inject constructor(
    private val context: Context,
    private val listener: WifiListener
) {
    fun discover(ssid: String, pass: String) {
        //val task = EsptouchTask(mySsid, myBssid, myPass, context)
        val waitInSec = 2
        val timeOut = ScheduledThreadPoolExecutor(1)
        listener.NotifyTouch("", TouchState.IN_PROGRESS)
        timeOut.schedule({
            Log.i(TAG, "timeout ")
            listener.NotifyTouch("", TouchState.TIMEOUT)
            //task.interrupt()
        }, waitInSec.toLong(), TimeUnit.SECONDS)
        //val task = EspTouchTask(ssid, "",pass, context)
        //task.setPackageBroadcast(false) // if true send broadcast packets, else send multicast packets
        //task.setEsptouchListener { result ->
        //    val newId: String = result.bssid
        //    Log.i(TAG, "newId: [$newId]")
         //   timeOut.isTerminated    //remove()  .cancel()
        //}
     }
}