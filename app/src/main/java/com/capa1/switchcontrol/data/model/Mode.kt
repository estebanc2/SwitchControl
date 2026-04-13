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

    fun hasIntValue() = this == Mode.PULSE_NA || this == Mode.PULSE_NC
            || this == Mode.TIMERS_TEMP || this == Mode.TEMP

    fun isGradient() = this == Mode.TIMERS_TEMP || this == Mode.TEMP
}