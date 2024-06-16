package com.capa1.switchcontrol.data


import androidx.compose.ui.graphics.Color
import com.capa1.switchcontrol.data.model.EspData
import com.capa1.switchcontrol.data.model.SwMode
import com.capa1.switchcontrol.data.model.SwState
import com.capa1.switchcontrol.data.model.WeeklyProgram
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
    val MyColors: Map<String, MyColor> = mapOf(
        "nothing"    to  MyColor(Color(0xFFFFFFFF), Color(0xFF000000)),
        "lemon"   to  MyColor(Color(0xFFEBF37E), Color(0xFF000000)),
        "grapefruit"  to  MyColor(Color(0xFFF7CA61), Color(0xFF000000)),
        "orange" to  MyColor(Color(0xFFFB8C00), Color(0xFF000000)),
        "rose"    to  MyColor(Color(0xFFFAACC9), Color(0xFF000000)),
        "lilac"    to  MyColor(Color(0xFFCA57E9), Color(0xFFFFFFFF)),
        "sky"   to  MyColor(Color(0xFF85D0F5), Color(0xFF000000)),
        "sea"     to  MyColor(Color(0xFF4F66F5), Color(0xFFFFFFFF)),
        "avocado"   to  MyColor(Color(0xFFA3F8A7), Color(0xFF000000)),
        "grass"   to  MyColor(Color(0xFF05800A), Color(0xFFFFFFFF)),
        "metal"   to  MyColor(Color(0xFF8F8C8F), Color(0xFFFFFFFF)),
        "wood"  to  MyColor(Color(0xFF793E2B), Color(0xFFFFFFFF)),
    )
    val NO_TIMERS: MutableList<WeeklyProgram> = mutableListOf ( WeeklyProgram(0,0,0),
                                                                WeeklyProgram(0,0,0),
                                                                WeeklyProgram(0,0,0),
                                                                WeeklyProgram(0,0,0)
                                                            )

    val SEND_ON: String = gson.toJson(
                    EspData(
                    name = "",
                    state = SwState.ON.ordinal,
                    mode = SwMode.TIMERS.ordinal,
                    secs = 0,
                    prgs = NO_TIMERS,
                    tempX10 = 0)
                )
    val SEND_OFF: String = gson.toJson(
        EspData(
            name = "",
            state = SwState.OFF.ordinal,
            mode = SwMode.TIMERS.ordinal,
            secs = 0,
            prgs =  NO_TIMERS,
            tempX10 = 0)
    )
    val SEND_GET: String = gson.toJson(
        EspData(
            name = "",
            state = SwState.GET_DATA.ordinal,
            mode = SwMode.TIMERS.ordinal,
            secs = 0,
            prgs =  NO_TIMERS,
            tempX10 = 0)
    )
    val SEND_ERASE: String = gson.toJson(
        EspData(
            name = "",
            state = SwState.ERASE.ordinal,
            mode = SwMode.TIMERS.ordinal,
            secs = 0,
            prgs =  NO_TIMERS,
            tempX10 = 0)
    )
}
