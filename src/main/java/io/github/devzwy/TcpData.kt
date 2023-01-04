package io.github.devzwy


/**
 * 封包读写实体类
 * 读取封包时传入不带包头和总长度的数据，每调用一次读取api
 * [startIndex]会自动递增，不需要的数据可以使用[stepReadLength]跳过
 * 写封包时按顺序写入，写入完成后调用[compile]传入包头即可返回完整的带77 90的封包
 */
class TcpData(var data: ByteArray = byteArrayOf()) {

    private val HEAD_BYTE_ARRAY = byteArrayOf(77, 90, 0, 0, 0, 0, 0, 0)

    //读封包的索引，仅在读取时内部自动增加，写入时没哟哦那个
    private var startIndex = 0

    /**
     * 写入字节数组
     */
    fun write(byteArray: ByteArray, bSize: Int = 0) {
        add(
            when (bSize) {
                0 -> byteArray
                1 -> byteArray.size.toByteArray1().plus(byteArray)
                2 -> byteArray.size.toByteArray2().plus(byteArray)
                4 -> byteArray.size.toByteArray4().plus(byteArray)
                else -> throw RuntimeException("参数错误")
            }
        )
    }

    /**
     * 写入字符串
     */
    fun write(string: String, bSize: Int = 0) {
        val sGBKByteArray = string.toGBKByteArray()
        add(
            when (bSize) {
                0 -> sGBKByteArray
                1 -> sGBKByteArray.size.toByteArray1().plus(sGBKByteArray)
                2 -> sGBKByteArray.size.toByteArray2().plus(sGBKByteArray)
                4 -> sGBKByteArray.size.toByteArray4().plus(sGBKByteArray)
                else -> throw RuntimeException("参数错误")
            }
        )
    }

    /**
     * 写入int
     * [bSize] 占用字节长度
     */
    fun write(int: Int, bSize: Int = 1) {
        add(
            when (bSize) {
                1 -> int.toByteArray1()
                2 -> int.toByteArray2()
                4 -> int.toByteArray4()
                else -> throw RuntimeException("参数错误")
            }
        )
    }


    /**
     * 封包添加结束时调用 自动增加头 77 90 和总长度以及头数据
     */
    fun compile(hexHead: String) {
        hexHead.hex2ByteArray().plus(data).let { allDataAndHead ->
            this.data = HEAD_BYTE_ARRAY.plus(allDataAndHead.size.toByteArray2()).plus(allDataAndHead)
        }
    }

    /**
     * 封包添加结束时调用 自动增加头 77 90 和总长度以及头数据
     */
    fun compile(byteArrayHead: ByteArray) {
        byteArrayHead.plus(data).let { allDataAndHead ->
            this.data = HEAD_BYTE_ARRAY.plus(allDataAndHead.size.toByteArray2()).plus(allDataAndHead)
        }
    }

    /**
     * 读出字节数组
     * [lengthSize] 指定字节数组数据位长度
     */
    fun readByteArray(lengthSize: Int = 1) = splitByteArray(lengthSize)

    /**
     * 读出字符串
     * [lengthSize] 指定字符串数据位长度
     */
    fun readString(lengthSize: Int = 1) = splitByteArray(readInt(lengthSize)).toGBKString()

    /**
     * 读取int
     */
    fun readInt(lengthSize: Int = 1) = splitByteArray(lengthSize).byteArrayToInt(0)

    /**
     * 读取十六进制
     */
    fun readHex(lengthSize: Int = 1) = splitByteArray(lengthSize).toHexString(false)

    /**
     * 读取剩余的数据
     */
    fun readLastData() = splitByteArray(data.size - startIndex)

    /**
     * 跳过读取字节数
     */
    fun stepReadLength(stepLength: Int) {
        startIndex += stepLength
    }

    private fun splitByteArray(lengthSize: Int): ByteArray {
        val lengthByteArray = ByteArray(lengthSize)
        System.arraycopy(data, startIndex, lengthByteArray, 0, lengthSize.also {
            startIndex += it
        })
        return lengthByteArray
    }

    private fun add(byteArray: ByteArray) {
        this.data = this.data.plus(byteArray)
    }
}