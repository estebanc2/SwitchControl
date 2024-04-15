package com.capa1.switchcontrol

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun getMacFromTopic(){
        val topic = "/mtc/to_sw/1018A"
        val mac = topic.split("/").last()
        println(String.format("%s es realmente 1018A", mac))
        assert(mac == "1018A")
    }
}