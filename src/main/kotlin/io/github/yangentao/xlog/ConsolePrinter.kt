package io.github.yangentao.xlog

object ConsolePrinter : LogPrinter {
    override fun printItem(item: LogItem) {
        when (item.level) {
            LogLevel.VERBOSE -> println(SGR("2") + SGR("3") + item.toString() + SGR("0"))
            LogLevel.INFO -> println(item.toString().sgr("1"))
            LogLevel.WARN -> println(item.toString().sgr("33"))
            LogLevel.ERROR, LogLevel.FATAIL -> println(item.toString().sgr("31"))
            else -> println(item)
        }
    }

}
