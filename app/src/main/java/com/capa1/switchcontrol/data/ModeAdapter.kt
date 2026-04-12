package com.capa1.switchcontrol.data

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader  // ← este
import com.google.gson.stream.JsonWriter  // ← y este
import com.capa1.switchcontrol.data.model.Mode

class ModeAdapter: TypeAdapter<Mode>() {
    override fun write(out: JsonWriter, value: Mode?) {
        out.value(value?.ordinal ?: 0)
    }
    override fun read(input: JsonReader): Mode = Mode.fromInt(input.nextInt())
}
