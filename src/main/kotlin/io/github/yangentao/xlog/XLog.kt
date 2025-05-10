@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.yangentao.xlog

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */
enum class LogLevel(val value: Int) {
    DEBUG(0), VERBOSE(1), INFO(2), WARN(3), ERROR(4), FATAIL(5);
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

object XLog {
    private var printer: LogPrinter = ConsolePrinter
    private var formatter: LogItemFormatter = DefaultLogItemFormatter
    var enabled = true
    var TAG: String = "xlog"

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
        printer.flush()
    }

    @Synchronized
    fun printItem(item: LogItem) {
        if (enabled) {
            printer.printItem(item)
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

object DefaultLogItemFormatter : LogItemFormatter {
    override fun format(item: LogItem): String {
        val sb = StringBuilder(item.message.length + 64)
        sb.append("TIM:")
        val date =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date(item.tm))
        sb.append(date)
        sb.append(" TID:")
        sb.append(String.format(Locale.getDefault(), "%6d", item.tid))
        sb.append(" LVL:")
        sb.append(item.level.name.first())
        sb.append(" TAG:")
        sb.append(item.tag)
        sb.append(" MSG:")
        sb.append(item.message)
        return sb.toString()
    }
}