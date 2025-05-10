@file:Suppress("unused")

package io.github.yangentao.xlog

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */
fun fatal(vararg args: Any?) {
    XLog.e(*args)
    XLog.flush()
    error(anyArrayToString(args))
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

class TagLog(val tag: String) {
    fun v(vararg args: Any?) {
        XLog.printItem(LogLevel.VERBOSE, tag, anyArrayToString(args))
    }

    fun d(vararg args: Any?) {
        XLog.printItem(LogLevel.DEBUG, tag, anyArrayToString(args))
    }

    fun i(vararg args: Any?) {
        XLog.printItem(LogLevel.INFO, tag, anyArrayToString(args))
    }

    fun w(vararg args: Any?) {
        XLog.printItem(LogLevel.WARN, tag, anyArrayToString(args))
    }

    fun e(vararg args: Any?) {
        XLog.printItem(LogLevel.ERROR, tag, anyArrayToString(args))
        XLog.flush()
    }

}