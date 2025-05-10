@file:Suppress("unused")

package io.github.yangentao.xlog

import io.github.yangentao.xlog.XLog.flush

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

fun logd(vararg args: Any?) {
    XLog.d(*args)
}

fun logi(vararg args: Any?) {
    XLog.i(*args)
}

fun loge(vararg args: Any?) {
    XLog.e(*args)
}

fun logx(tag: String, vararg args: Any?) {
    XLog.dx(tag, *args)
}

class TagLog(val tag: String) {
    fun d(vararg args: Any?) {
        XLog.printItem(LogLevel.DEBUG, tag, anyArrayToString(args))
    }

    fun w(vararg args: Any?) {
        XLog.printItem(LogLevel.WARN, tag, anyArrayToString(args))
    }

    fun e(vararg args: Any?) {
        XLog.printItem(LogLevel.ERROR, tag, anyArrayToString(args))
        flush()
    }

    fun i(vararg args: Any?) {
        XLog.printItem(LogLevel.INFO, tag, anyArrayToString(args))
    }
}