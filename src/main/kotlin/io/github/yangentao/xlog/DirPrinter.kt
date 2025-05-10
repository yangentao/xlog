package io.github.yangentao.xlog

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

@Suppress("unused")
class DirPrinter(private val dir: File, private val keepDays: Int = 30) : LogPrinter {
    private var filePrinter: FilePrinter? = null
    private var dayOfYear: Int = 0
    private var disposed = false
    private val reg = Regex("\\d{4}-\\d{2}-\\d{2}\\.log")

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    @Synchronized
    override fun dispose() {
        disposed = true
        filePrinter?.dispose()
        filePrinter = null;
    }

    @Synchronized
    override fun flush() {
        filePrinter?.flush()
    }

    @Synchronized
    override fun printItem(item: LogItem) {
        checkFile()
        filePrinter?.printItem(item)
    }

    @Suppress("UNUSED_PARAMETER")
    @Synchronized
    private fun checkFile() {
        if (!disposed) {
            return
        }
        val days = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if (dayOfYear == days && filePrinter != null) {
            return
        }
        dayOfYear = days
        filePrinter?.dispose()
        filePrinter = null

        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val ds = fmt.format(Date(System.currentTimeMillis()))
        filePrinter = FilePrinter(File(dir, "$ds.log"))

        deleteLogs()
    }

    private fun deleteLogs() {
        if (keepDays <= 0) {
            return
        }
        val n = keepDays
        val fs = dir.listFiles() ?: return
        val ls = fs.filter { it.name.matches(reg) }.sortedByDescending { it.name }
        if (ls.size > n + 1) {
            for (i in (n + 1) until ls.size) {
                ls[i].delete()
            }
        }
    }
}