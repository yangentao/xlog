package io.github.yangentao.xlog

import java.io.File
import java.io.PrintWriter

interface LogPrinter {
    fun printItem(item: LogItem)
    fun flush() {}
    fun dispose() {}
}

object EmptyPrinter : LogPrinter {
    override fun printItem(item: LogItem) {}
}

object ConsolePrinter : LogPrinter {
    var level: LogLevel = LogLevel.ALL
    override fun printItem(item: LogItem) {
        if (item.level < level) return
        when (item.level) {
            LogLevel.VERBOSE -> println(SGR("2") + SGR("3") + item.toString() + SGR("0"))
            LogLevel.INFO -> println(item.toString().sgr("1"))
            LogLevel.WARN -> println(item.toString().sgr("33"))
            LogLevel.ERROR, LogLevel.FATAIL -> println(item.toString().sgr("31"))
            else -> println(item)
        }
    }

}

class FilePrinter(val file: File) : LogPrinter {
    val pw: PrintWriter = file.printWriter()

    @Synchronized
    override fun printItem(item: LogItem) {
        pw.println(item.toString())
    }

    @Synchronized
    override fun flush() {
        pw.flush()
    }

    @Synchronized
    override fun dispose() {
        pw.flush()
        pw.close()
    }
}

class FilterPrinter(val printer: LogPrinter, val filter: LogFilter) : LogPrinter, LogFilter {
    override fun printItem(item: LogItem) {
        if (accept(item)) printer.printItem(item)
    }

    override fun flush() {
        printer.flush()
    }

    override fun dispose() {
        printer.dispose()
    }

    override fun accept(item: LogItem): Boolean {
        return filter.accept(item)
    }
}

class TreePrinter(vararg ps: LogPrinter) : LogPrinter {
    private val list: ArrayList<LogPrinter> = arrayListOf(*ps)

    override fun printItem(item: LogItem) {
        for (p in list) {
            p.printItem(item)
        }
    }

    fun add(p: LogPrinter) {
        list.add(p)
    }

    fun remove(p: LogPrinter) {
        list.removeIf { it == p }
        p.dispose()
    }

    override fun flush() {
        for (p in list) {
            p.flush()
        }
    }

    override fun dispose() {
        for (p in list) {
            p.flush()
            p.dispose()
        }
        list.clear()
    }

}