@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.yangentao.xlog

import java.util.*

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */

object XLog {
    private var formatter: LogFormatter = DefaultLogFormatter
    private var printer: LogPrinter = ConsolePrinter
    private var filter: LogFilter = LevelFilter(LogLevel.ALL)
    private var count = 0
    var TAG: String = "xlog"
    var level: LogLevel = LogLevel.ALL

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
    fun setFormatter(f: LogFormatter) {
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
        if (item.level.ge(level) && filter.accept(item)) {
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

    fun v(vararg args: Any?) {
        printItem(LogLevel.VERBOSE, TAG, logArrayToString(args))
    }

    fun d(vararg args: Any?) {
        printItem(LogLevel.DEBUG, TAG, logArrayToString(args))
    }

    fun i(vararg args: Any?) {
        printItem(LogLevel.INFO, TAG, logArrayToString(args))
    }

    fun w(vararg args: Any?) {
        printItem(LogLevel.WARN, TAG, logArrayToString(args))
    }

    fun e(vararg args: Any?) {
        printItem(LogLevel.ERROR, TAG, logArrayToString(args))
        flush()
    }

}


