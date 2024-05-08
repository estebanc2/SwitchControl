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

    const val TAG = "switchcontrol"
    const val FLASH_VERSION = "1.8"
    const val ESPTOUCH_WAIT_IN_SECS = 5L
    data class MyColor(
        val backColor: Color,
        val textColor: Color
    )
    val MyColors: Map<String, MyColor> = mapOf(
        "nada"    to  MyColor(Color(0xFFFFFFFF), Color(0xFF000000)),
        "limon"   to  MyColor(Color(0xFFFDD835), Color(0xFF000000)),
        "pomelo"  to  MyColor(Color(0xFFFFAA00), Color(0xFF000000)),
        "naranja" to  MyColor(Color(0xFFFB8C00), Color(0xFF000000)),
        "rosa"    to  MyColor(Color(0xFFFCA0C2), Color(0xFF000000)),
        "lila"    to  MyColor(Color(0xFFCA57E9), Color(0xFFFFFFFF)),
        "cielo"   to  MyColor(Color(0xFF85D0F5), Color(0xFF000000)),
        "mar"     to  MyColor(Color(0xFF4F66F5), Color(0xFFFFFFFF)),
        "palta"   to  MyColor(Color(0xFFA3F8A7), Color(0xFF000000)),
        "pasto"   to  MyColor(Color(0xFF05800A), Color(0xFFFFFFFF)),
        "metal"   to  MyColor(Color(0xFF8F8C8F), Color(0xFFFFFFFF)),
        "madera"  to  MyColor(Color(0xFF793E2B), Color(0xFFFFFFFF)),
    )
    val NO_TIMERS: MutableList<WeeklyProgram> = mutableListOf( WeeklyProgram(0,0,0),
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
/*
SwData("velador", "483fda877368", 2, "limon", SwStatus.DISCONNECTED),
SwData("luz cocina", "98f4abb33d5a", 1, "lila", SwStatus.DISCONNECTED),
SwData("riego", "483fda878e46", 3, "pasto", SwStatus.DISCONNECTED),
SwData("alargue", "bcddc247dbc9", 5, "palta", SwStatus.DISCONNECTED),
SwData("TV", "483fda879484", 4, "madera", SwStatus.DISCONNECTED)
*/
