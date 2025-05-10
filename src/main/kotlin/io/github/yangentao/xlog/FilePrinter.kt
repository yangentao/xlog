package io.github.yangentao.xlog

import java.io.File
import java.io.FileOutputStream

class FilePrinter(val file: File) : LogPrinter {
    val fos: FileOutputStream = file.outputStream()

    override fun printItem(item: LogItem) {
        fos.write(item.toString().toByteArray())
    }

    override fun flush() {
        fos.flush()
    }

    override fun dispose() {
        fos.close()
    }
}