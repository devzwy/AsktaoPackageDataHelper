package io.github.devzwy

import com.alibaba.fastjson2.JSON

//常用扩展函数的封装

/**
 * 任意对象转json字符串
 */
fun Any.toJson() = JSON.toJSONString(this)

