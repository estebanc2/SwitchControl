package com.capa1.switchcontrol.data.model

enum class State {
    OFF,
    ON,
    GET_DATA,
    SET_DATA,
    ERASE,
    UPGRADE,
    SERVER_FAIL,
    UPGRADE_FAIL,
    UPGRADED,
    ALREADY_LATEST,
    AUTO_OFF;
    companion object {
        fun fromInt(value: Int): State = entries.getOrElse(value) { OFF }
    }
}
