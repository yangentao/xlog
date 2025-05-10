package io.github.yangentao.xlog

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

@Suppress("unused")
class DirPrinter(private val dir: File, private val keepDays: Int) : LogPrinter {

    private var writer: BufferedWriter? = null
    private var day: Int = 0

    private var disposed = false
    private val reg = Regex("\\d{4}-\\d{2}-\\d{2}.*\\.log")

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    @Synchronized
    override fun dispose() {
        if (!disposed) return
        disposed = true
        writer?.flush()
        writer?.close()
        writer = null
    }

    @Synchronized
    override fun flush() {
        try {
            writer?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Synchronized
    private fun peekWriter(tag: String): BufferedWriter? {
        if (!disposed) {
            return null
        }
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val oldW = writer
        if (day == dayOfYear && oldW != null) {
            return oldW
        }
        oldW?.flush()
        oldW?.close()
        deleteOldLogs()

        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val ds = fmt.format(Date(System.currentTimeMillis()))
        val filename: String = "$ds.log"
        try {
            val writer = BufferedWriter(FileWriter(File(dir, filename), true), 16 * 1024)
            this.writer = writer
            day = dayOfYear
            return writer
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    private fun deleteOldLogs() {
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

    override fun printItem(item: LogItem) {
        val w = peekWriter(item.tag) ?: return
        try {
            w.write(item.toString())
            w.write("\n")
        } catch (e: IOException) {
            w.close()
            e.printStackTrace()
            writer = null
        }
    }
}