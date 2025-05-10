package io.github.yangentao.xlog

object ConsolePrinter : LogPrinter {
    override fun printItem(item: LogItem) {
        when (item.level) {
            LogLevel.VERBOSE -> println(SGR("2") + SGR("3") + item.toString() + SGR("0"))
            LogLevel.INFO -> println(SGR("1") + item.toString() + SGR("0"))
            LogLevel.WARN -> println(SGR("33") + item.toString() + SGR("0"))
            LogLevel.ERROR, LogLevel.FATAIL -> println(SGR("31") + item.toString() + SGR("0"))
            else -> println(item)
        }
    }

}
