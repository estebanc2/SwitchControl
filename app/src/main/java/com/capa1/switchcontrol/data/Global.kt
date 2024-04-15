package com.capa1.switchcontrol.data

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
    val SEND_ON = gson.toJson(
                    EspData(
                    name = "",
                    state = SwState.ON.ordinal,
                    mode = SwMode.TIMERS.ordinal,
                    modeAux = 0,
                    swPrograms = listOf( WeeklyProgram(0,0,0),
                        WeeklyProgram(0,0,0),
                        WeeklyProgram(0,0,0),
                        WeeklyProgram(0,0,0)
                    ),
                    tempX10 = 0)
                )
    val SEND_OFF = gson.toJson(
        EspData(
            name = "",
            state = SwState.OFF.ordinal,
            mode = SwMode.TIMERS.ordinal,
            modeAux = 0,
            swPrograms = listOf( WeeklyProgram(0,0,0),
                WeeklyProgram(0,0,0),
                WeeklyProgram(0,0,0),
                WeeklyProgram(0,0,0)
            ),
            tempX10 = 0)
    )
    val SEND_GET = gson.toJson(
        EspData(
            name = "",
            state = SwState.GET_DATA.ordinal,
            mode = SwMode.TIMERS.ordinal,
            modeAux = 0,
            swPrograms = listOf( WeeklyProgram(0,0,0),
                WeeklyProgram(0,0,0),
                WeeklyProgram(0,0,0),
                WeeklyProgram(0,0,0)
            ),
            tempX10 = 0)
    )



}