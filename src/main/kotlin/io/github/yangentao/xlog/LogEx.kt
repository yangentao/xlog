@file:Suppress("unused")

package io.github.yangentao.xlog

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */
fun fatal(vararg args: Any?) {
    XLog.e(*args)
    XLog.flush()
    error(logArrayToString(args))
}

fun logv(vararg args: Any?) {
    XLog.v(*args)
}

fun logd(vararg args: Any?) {
    XLog.d(*args)
}

fun logi(vararg args: Any?) {
    XLog.i(*args)
}

fun logw(vararg args: Any?) {
    XLog.w(*args)
}

fun loge(vararg args: Any?) {
    XLog.e(*args)
}
