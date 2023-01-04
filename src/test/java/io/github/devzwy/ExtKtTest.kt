package io.github.devzwy

import org.junit.Test

import org.junit.Assert.*

class ExtKtTest {

    private data class TestToJsonBean(val a: String, val b: Int)

    private val JSON_STR = "{\"a\":\"str\",\"b\":888}"
    private val jsonDataBean = TestToJsonBean("str", 888)
    private val testByteArratDataInt = 2
    private val testByteArratDataStr = "模拟字符串"
    private val testByteArratDataBa = byteArrayOf(1, 2, 3)
    private val hexDataBa = "010203"

    private val testByteArrayData = byteArrayOf(77, 90, 0, 0, 0, 0, 0, 0, 0, 19, 0, 1, 0, 2, 0, 10, -60, -93, -60, -30, -41, -42, -73, -5, -76, -82, 1, 2, 3)

    @Test
    fun toJson() {
        assertEquals(JSON_STR, jsonDataBean.toJson())
    }

    @Test
    fun writeTcpData() {
        assertEquals(writeTcpData {
            //模拟ID 占用2字节
            write(testByteArratDataInt, 2)
            //模拟字符串写入，长度字段占用2个字节长度
            write(testByteArratDataStr, 2)
            //模拟写入一个字节数组，不占用长度位
            write(testByteArratDataBa)
            //模拟一个封包头 0,1 为模拟的包头
            compile("0001")
        }.toHexString(false), testByteArrayData.toHexString(false))
    }

    @Test
    fun readTcpData() {
        readTcpData(testByteArrayData) {
            //跳过前面固定位置4字节
            stepReadLength(4)
            //读出4位字节长度的时间戳
            val time = readInt(4)
            //读取2字节的封包总长度
            val allDataSize = readInt(2)
            //读出两位字节长度的包头
            val head = readInt(2)

            //读取ID 读出2位长度的int值
            val id = readInt(2)
            //读取字符串 长度位占用2字节
            val testStr = readString(2)
            //读取3字节内容
            val tmpTba = readByteArray(3)

            assertEquals(id, testByteArratDataInt)
            assertEquals(testStr, testByteArratDataStr)
            assertEquals(tmpTba.toHexString(false), testByteArratDataBa.toHexString(false))
        }
    }

    @Test
    fun copy() {
        assertEquals(testByteArrayData.copy(0, 2).toHexString(false), byteArrayOf(77, 90).toHexString(false))
    }

    @Test
    fun toByteArray4() {
        assertEquals(testByteArratDataInt.toByteArray4().toHexString(false), "00000002")
    }

    @Test
    fun toByteArray1() {
        assertEquals(testByteArratDataInt.toByteArray1().toHexString(false), "02")
    }

    @Test
    fun toByteArray2() {
        assertEquals(testByteArratDataInt.toByteArray2().toHexString(false), "0002")
    }

    @Test
    fun toHexString_hex2ByteArray() {
        assertEquals(testByteArratDataBa.toHexString(false), hexDataBa)
    }

    @Test
    fun toGBKString_toGBKByteArray() {
        assertEquals(testByteArratDataStr, testByteArratDataStr.toGBKByteArray().toGBKString())
    }


    @Test
    fun hexToInt() {
        assertEquals(2, "02".hexToInt())
    }

}