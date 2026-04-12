package com.capa1.switchcontrol.data

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader  // ← este
import com.google.gson.stream.JsonWriter  // ← y este
import com.capa1.switchcontrol.data.model.State

class StateAdapter: TypeAdapter<State>() {
    override fun write(out: JsonWriter, value: State?) {
        out.value(value?.ordinal ?: 0)
    }
    override fun read(input: JsonReader): State = State.fromInt(input.nextInt())
}
