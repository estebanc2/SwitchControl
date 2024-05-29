package com.capa1.switchcontrol.data.model

data class StoredData (
    val name: String,
    val id: String,
    val bkColor: String
)

data class ToStore (
    val list: MutableList<StoredData>
)