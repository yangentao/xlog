@file:Suppress("unused")

package io.github.yangentao.xlog

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

fun logd(vararg args: Any?) {
    LogX.d(*args)
}

fun logi(vararg args: Any?) {
    LogX.i(*args)
}

fun loge(vararg args: Any?) {
    LogX.e(*args)
}


fun logx(tag: String, vararg args: Any?) {
    LogX.dx(tag, *args)
}

fun logxd(tag: String, vararg args: Any?) {
    LogX.dx(tag, *args)
}

fun logxi(tag: String, vararg args: Any?) {
    LogX.ix(tag, *args)
}

fun logxe(tag: String, vararg args: Any?) {
    LogX.ex(tag, *args)
}