package com.capa1.switchcontrol.data.model

data class StoredData (
    val name: String,
    val id: String,
    val icon: String
)

data class ToStore (
    val list: List<StoredData>
)
/*
val TEST: ToStore = ToStore(listOf (
        StoredData(name = "Velador", id = "483fda877368", icon = "lamp.ceiling"),
        StoredData(name = "Luz cocina", id = "98f4abb33d5a", icon = "lightbulb"),
        StoredData(name = "Ventilador", id = "3083988f1361", icon = "fan"),
        StoredData(name = "Riego", id = "483fda878e46", icon = "drop"),
        StoredData(name = "Filtro", id = "3083988f155e", icon = "scope"),
        StoredData(name = "TV", id = "483fda879484", icon = "tv"),
        StoredData(name = "Arbolito", id = "bcddc247dbc9", icon = "powercord"),
        StoredData(name = "Calefa", id = "a4cf12f00ab0", icon = "speedometer"),
        StoredData(name = "Termo", id = "a5cf12f00ab0", icon = "thermometer")
        )
    )*/

