package io.github.yangentao.xlog

class TagLog(val tag: String) {
    var level: LogLevel = LogLevel.ALL

    fun v(vararg args: Any?) {
        if (level.ge(LogLevel.VERBOSE)) XLog.printItem(LogLevel.VERBOSE, tag, logArrayToString(args))
    }

    fun d(vararg args: Any?) {
        if (level.ge(LogLevel.DEBUG)) XLog.printItem(LogLevel.DEBUG, tag, logArrayToString(args))
    }

    fun i(vararg args: Any?) {
        if (level.ge(LogLevel.INFO)) XLog.printItem(LogLevel.INFO, tag, logArrayToString(args))
    }

    fun w(vararg args: Any?) {
        if (level.ge(LogLevel.WARN)) XLog.printItem(LogLevel.WARN, tag, logArrayToString(args))
    }

    fun e(vararg args: Any?) {
        if (level.ge(LogLevel.ERROR)) {
            XLog.printItem(LogLevel.ERROR, tag, logArrayToString(args))
            XLog.flush()
        }
    }

}