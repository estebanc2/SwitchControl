package com.capa1.switchcontrol.data


import androidx.compose.ui.graphics.Color
import com.google.gson.Gson

object Global {
    val gson = Gson()
    const val TO_SW = "/mtc/to_sw/"
    const val FROM_SW = "/mtc/from_sw/"
    const val MQTT_HOST_AND_PORT =
        "tcp://linode.1.poplarlabs.net:1883" //"tcp://test.mosquitto.org:1883"; //
    const val TAG = "switch control"
    const val ESPTOUCH_WAIT_IN_SECS = 18L

    data class MyColor(
        val backColor: Color,
        val textColor: Color
    )
}

