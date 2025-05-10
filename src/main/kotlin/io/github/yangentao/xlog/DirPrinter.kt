package io.github.yangentao.xlog

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

@Suppress("unused")
class DirPrinter(private val dir: File, val namePrefix: String = "", private val keepDays: Int = 30, fileSizeM: Long = 10) : LogPrinter {
    @Suppress("PrivatePropertyName")
    private val MAX_SIZE: Long = fileSizeM * 1_000_000
    private val nameRegex: Regex = Regex("$namePrefix\\d{4}-\\d{2}-\\d{2}.*\\.log")
    private var filePrinter: FilePrinter? = null
    private var dayOfYear: Int = 0
    private var disposed = false
    private var count = 0

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    @Synchronized
    override fun dispose() {
        count = 0
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
        count += 1
        checkFile()
        filePrinter?.printItem(item)
    }

    @Suppress("UNUSED_PARAMETER")
    @Synchronized
    private fun checkFile() {
        if (disposed) {
            return
        }

        if (filePrinter == null) {
            createNewFile()
            return
        }
        if (count > 1000) {
            val fileSize: Long = filePrinter?.file?.length() ?: 0L
            if (fileSize > MAX_SIZE) {
                createNewFile()
                return
            }
        }
        val days = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if (dayOfYear != days) {
            createNewFile()
            return
        }

    }

    private fun createNewFile() {
        dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        count = 0
        filePrinter?.dispose()
        filePrinter = null
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val ds = fmt.format(Date(System.currentTimeMillis()))
        var f = File(dir, "$namePrefix$ds.log")
        var i = 1;
        while (f.exists()) {
            f = File(dir, "$namePrefix$ds.$i.log")
            i += 1;
        }
        filePrinter = FilePrinter(f)

        Thread { deleteLogs() }.start()
    }

    private fun deleteLogs() {
        if (keepDays <= 0) {
            return
        }
        val fs = dir.listFiles()?.filter { it.name.matches(nameRegex) } ?: return
        val c: Calendar = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, -keepDays)
        val tm: Long = c.timeInMillis
        for (f in fs) {
            if (f.lastModified() < tm) {
                f.delete()
            }
        }
    }
}