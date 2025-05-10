@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.yangentao.xlog

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */
enum class LogLevel(val value: Int) {
    ALL(0), VERBOSE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FATAIL(6), OFF(9);
}

data class LogItem(val level: LogLevel, val tag: String, val message: String, val tid: Long, val tm: Long = System.currentTimeMillis()) {

    private val foramttedMessage: String by lazy { XLog.formatMessage(this) }

    override fun toString(): String {
        return foramttedMessage
    }
}

interface LogItemFormatter {
    fun format(item: LogItem): String
}

interface LogPrinter {
    fun printItem(item: LogItem)
    fun flush() {}
    fun dispose() {}
}

interface LogFilter {
    fun accept(item: LogItem): Boolean
}

object XLog {
    private var printer: LogPrinter = ConsolePrinter
    private var formatter: LogItemFormatter = DefaultLogItemFormatter
    private var filter: LogFilter = LevelFilter(LogLevel.ALL)
    private var count = 0
    var TAG: String = "xlog"
    var enabled = true

    private val timer: Timer = Timer("logtimer", true)
    private val flushTask = object : TimerTask() {
        override fun run() {
            try {
                if (count > 0) {
                    flush()
                }
            } catch (ex: Throwable) {
            }
        }

    }

    init {
        timer.scheduleAtFixedRate(flushTask, 2000, 2000)
        Runtime.getRuntime().addShutdownHook(Thread {
            timer.cancel()
            flush()
        }.also { it.isDaemon = true })
    }

    fun setFilter(filter: LogFilter) {
        this.filter = filter
    }

    @Synchronized
    fun formatMessage(item: LogItem): String {
        return formatter.format(item)
    }

    @Synchronized
    fun setFormatter(f: LogItemFormatter) {
        this.formatter = f;
    }

    @Synchronized
    fun setPrinter(p: LogPrinter) {
        printer.dispose()
        printer = p
    }

    @Synchronized
    fun removePrinter() {
        printer.dispose()
        printer = EmptyPrinter
    }

    @Synchronized
    fun flush() {
        count = 0
        printer.flush()
    }

    @Synchronized
    fun printItem(item: LogItem) {
        if (enabled && filter.accept(item)) {
            printer.printItem(item)
            count += 1
            if (count > 20) {
                flush()
            }
        }
    }

    @Suppress("DEPRECATION")
    fun printItem(level: LogLevel, tag: String, msg: String) {
        val item = LogItem(level, tag, msg, Thread.currentThread().id, System.currentTimeMillis())
        printItem(item)
    }

    fun d(vararg args: Any?) {
        printItem(LogLevel.DEBUG, TAG, anyArrayToString(args))
    }

    fun w(vararg args: Any?) {
        printItem(LogLevel.WARN, TAG, anyArrayToString(args))
    }

    fun e(vararg args: Any?) {
        printItem(LogLevel.ERROR, TAG, anyArrayToString(args))
        flush()
    }

    fun i(vararg args: Any?) {
        printItem(LogLevel.INFO, TAG, anyArrayToString(args))
    }

    fun dx(tag: String, vararg args: Any?) {
        printItem(LogLevel.DEBUG, tag, anyArrayToString(args))
    }

    fun wx(tag: String, vararg args: Any?) {
        printItem(LogLevel.WARN, tag, anyArrayToString(args))
    }

    fun ex(tag: String, vararg args: Any?) {
        printItem(LogLevel.ERROR, tag, anyArrayToString(args))
        flush()
    }

    fun ix(tag: String, vararg args: Any?) {
        printItem(LogLevel.INFO, tag, anyArrayToString(args))
    }

}

object EmptyPrinter : LogPrinter {
    override fun printItem(item: LogItem) {}
}

object ConsolePrinter : LogPrinter {
    override fun printItem(item: LogItem) {
        println(item)
    }

}

class LevelFilter(val level: LogLevel) : LogFilter {
    override fun accept(item: LogItem): Boolean {
        return item.level.value >= level.value
    }

}

object DefaultLogItemFormatter : LogItemFormatter {
    override fun format(item: LogItem): String {
        val sb = StringBuilder(item.message.length + 64)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date(item.tm))
        sb.append(date)
//        sb.append(String.format(Locale.getDefault(), " [%4d] ", item.tid))
        sb.append(" ${item.tid} ")
        sb.append(item.level.name.first())
        sb.append(" ")
        sb.append(item.tag)
        sb.append(": ")
        sb.append(item.message)
        return sb.toString()
    }
}