package com.capa1.switchcontrol

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
data class Element(
    val a:String,
    var b: String
)
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
    @Test
    fun listModif(){
        val element1 = Element ("carlos", "Castro")
        val element2 = Element("pilar", "samanes")
        val element3 = Element("oti", "castro")
        var testList = listOf(element1, element2, element3)
        testList.forEachIndexed {index, element ->
            if(element.a == "pilar"){
                testList[index].b = "de castro"
            }
        }
        assert(testList[1].b == "de castro")
    }
    @Test
    fun listInsert(){
        val myList = mutableListOf( 1, 2, 3, 4, 5, 6)
        val qty = myList.size
        val element = 4
        val newRow = 6
        myList.remove(element)
        myList.add(newRow -1, element)
        for (element in myList){
            println(String.format("%d",element))
        }
        assert(myList[newRow - 1] == element)
    }
    @Test
    fun listItemRemove() {
        val element1 = Element ("carlos", "Castro")
        val element2 = Element("pilar", "samanes")
        val element3 = Element("oti", "castro")
        var testList = mutableListOf(element1, element2, element3)
        val initSize = testList.size
        testList.removeIf{it.b == "samanes"}
        println(String.format("%d",testList.size))
        assert(initSize > testList.size)
    }
    @Test
    fun listItemSet() {
        val element1 = Element ("carlos", "Castro")
        val element2 = Element("pilar", "samanes")
        val element3 = Element("oti", "castro")
        var testList = mutableListOf(element1, element2, element3)
        testList[testList.indexOfFirst {it.b == "samanes"}] = Element("Angelina","Holly")
        println(String.format("%s", testList[1].b))
        assert(testList[1].b == "Holly")
    }
    @Test
    fun removeItems(){
        var mylist = mutableListOf ("uno", "dos")
        mylist -= "uno"
        mylist -= "dos"
        mylist -= "uno"
        assert(mylist.size == 0)
    }
    @Test
    fun convertByte(){
        val string = "feac3455ff"
        val ss = string.substring(0,2).toInt(16)//.toUByte()//.toByte()
        println(String.format("%d",ss))
        val rr = ss.toString(16) + string.substring(2,string.length)
        println(String.format("%s",rr))
    }
}
