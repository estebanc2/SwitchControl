package com.capa1.switchcontrol.data.model

enum class Mode {
    TIMERS,
    TIMERS_CONTACT,
    PULSE_NA,
    PULSE_NC,
    TIMERS_TEMP,
    TEMP;
    companion object {
        fun fromInt(value: Int): Mode = entries.getOrElse(value) { TIMERS }
    }
}