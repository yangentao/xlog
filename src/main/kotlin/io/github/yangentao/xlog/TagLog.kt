package io.github.yangentao.xlog

class TagLog(val tag: String) {
    var level: LogLevel = LogLevel.ALL

    private fun allow(lv: LogLevel): Boolean {
        return lv >= level
    }

    fun v(vararg args: Any?) {
        if (allow(LogLevel.VERBOSE)) XLog.printItem(LogLevel.VERBOSE, tag, logArrayToString(args))
    }

    fun d(vararg args: Any?) {
        if (allow(LogLevel.DEBUG)) XLog.printItem(LogLevel.DEBUG, tag, logArrayToString(args))
    }

    fun i(vararg args: Any?) {
        if (allow(LogLevel.INFO)) XLog.printItem(LogLevel.INFO, tag, logArrayToString(args))
    }

    fun w(vararg args: Any?) {
        if (allow(LogLevel.WARN)) XLog.printItem(LogLevel.WARN, tag, logArrayToString(args))
    }

    fun e(vararg args: Any?) {
        if (allow(LogLevel.ERROR)) {
            XLog.printItem(LogLevel.ERROR, tag, logArrayToString(args))
            XLog.flush()
        }
    }

}