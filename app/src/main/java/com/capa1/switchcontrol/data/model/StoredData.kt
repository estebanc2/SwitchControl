package com.capa1.switchcontrol.data.model

data class StoredData (
    val name: String,
    val id: String,
    val icon: String
)

data class ToStore (
    val list: MutableList<StoredData>
)
/*
val LIST_LOOK: ToStore = ToStore([StoredData(name = "Velador", id = "483fda877368", icon = "lamp.desk"),
        StoredData(name = "Luz cocina", id = "98f4abb33d5a", icon = "lightbulb.fill"),
        StoredData(name = "Ventilador", id = "3083988f1361", icon = "fan.fill"),
        StoredData(name = "Riego", id = "483fda878e46", icon = "drop.fill"),
        StoredData(name = "Filtro", id = "3083988f155e", icon = "leaf.fill"),
        StoredData(name = "TV", id = "483fda879484", icon = "tv.fill"),
        StoredData(name = "Arbolito", id = "bcddc247dbc9", icon = "leaf.fill"),
        StoredData(name = "Calefa", id = "a4cf12f00ab0", icon = "wind"),
        StoredData(name = "Termo", id = "a5cf12f00ab0", icon = "thermometer.medium")])

*/