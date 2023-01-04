package io.github.devzwy

import com.alibaba.fastjson2.JSON
import java.nio.charset.Charset

//常用扩展函数的封装

/**
 * 任意对象转json字符串
 * Json解析警告屏蔽:--illegal-access=deny
 */
fun Any.toJson() = JSON.toJSONString(this)

/**
 * 截取字节数组
 * [startIndex] 开始的位置
 * [lengthSize]截取的长度
 */
fun ByteArray.copy(startIndex: Int, lengthSize: Int): ByteArray {
    val lengthByteArray = ByteArray(lengthSize)
    System.arraycopy(this, startIndex, lengthByteArray, 0, lengthSize)
    return lengthByteArray
}

/**
 * int转4字节
 */
fun Int.toByteArray4(): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[0] = ((this shr 24) and 0xff).toByte()
    byteArray[1] = ((this shr 16) and 0xff).toByte()
    byteArray[2] = ((this shr 8) and 0xff).toByte()
    byteArray[3] = (this and 0xff).toByte()
    return byteArray
}

/**
 * int转1字节
 */
fun Int.toByteArray1(): ByteArray {
    val byteArray = ByteArray(1)
    val LowL = (this and 0xff).toByte()
    byteArray[0] = LowL
    return byteArray
}

/**
 * int转2字节
 */
fun Int.toByteArray2(): ByteArray {
    val byteArray = ByteArray(2)
    val LowH = ((this shr 8) and 0xff).toByte()
    val LowL = (this and 0xff).toByte()
    byteArray[0] = LowH
    byteArray[1] = LowL
    return byteArray
}


/**
 * 字节集转十六进制
 */
fun ByteArray.toHexString(hasSpace: Boolean = true) = this.joinToString("") {
    (it.toInt() and 0xFF).toString(16).padStart(2, '0').uppercase() + if (hasSpace) " " else ""
}

/**
 * 字节集转gbk编码字符串
 */
fun ByteArray.toGBKString() = toString(Charset.forName("GBK"))

/**
 * 字符串转gbk编码的字节数组
 */
fun String.toGBKByteArray() = toByteArray(Charset.forName("GBK"))

/**
 * 十六进制转字节数组
 */
fun String.hex2ByteArray(): ByteArray {
    val s = this.replace(" ", "")
    val bs = ByteArray(s.length / 2)
    for (i in 0 until s.length / 2) {
        bs[i] = s.substring(i * 2, i * 2 + 2).toInt(16).toByte()
    }
    return bs
}

/**
 * 将字节数组转换为十六进制并转换为int
 * [default] 默认值，在转换抛除异常时使用，不穿表示不进行异常捕获
 */
fun ByteArray.byteArrayToInt(default: Int? = null) = toHexString(false).hexToInt(default)

/**
 * 十六字符串转数字
 * [default] 默认值，在转换抛除异常时使用，不写表示不进行异常捕获
 */
fun String.hexToInt(default: Int? = null): Int {
    default?.let {
        return try {
            Integer.parseInt(this, 16)
        } catch (e: Exception) {
            e.printStackTrace()
            it
        }
    }

    return Integer.parseInt(this,16)

}

/**
 * 封包构造器
 * 按顺序一个一个add
 * 最后增加一次comple 加入头即可返回一整个封包
 */
fun writeTcpData(block: TcpData.() -> Unit): ByteArray {
    return TcpData().apply(block).data
}

/**
 * 封包构造器
 * 按顺序一个一个add
 * 最后增加一次comple 加入头即可返回一整个封包
 */
inline fun readTcpData(data: ByteArray, block: TcpData.() -> Unit) {
    TcpData(data).apply(block)
}