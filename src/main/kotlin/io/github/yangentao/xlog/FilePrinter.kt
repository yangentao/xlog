package io.github.yangentao.xlog

import java.io.File
import java.io.PrintWriter

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