package io.github.yangentao.xlog

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */
private const val MB: Long = 1024 * 1024

private fun Int.ge(v: Int): Int {
    if (this < v) return v
    return this
}

private fun Long.ge(v: Long): Long {
    if (this < v) return v
    return this
}

@Suppress("unused", "PrivatePropertyName")
class DirPrinter(
    private val dir: File,
    val baseName: String = "app",
    val extName: String = "log",
    private val maxDays: Int = 30,
    maxFileCount: Int? = null,
    fileSizeM: Long? = null,
    fileSize: Long? = null,
    checkLineCount: Int = 1000
) :
    LogPrinter {
    private val MAX_COUNT: Int = maxFileCount?.ge(1) ?: Int.MAX_VALUE
    private val MAX_SIZE: Long = fileSizeM?.ge(1L)?.let { it * MB } ?: fileSize?.ge(1000L) ?: (MB * 100L)
    private val MIN_LINES: Int = checkLineCount.ge(100)
    private val nameRegex: Regex = Regex("${baseName}_\\d{4}-\\d{2}-\\d{2}\\.\\d+\\.$extName")
    private var filePrinter: FilePrinter? = null
    private var lines = 0
    private var dayOfYear: Int = 0

    init {
        assert(baseName.isNotEmpty())
        assert(extName.isNotEmpty())
        assert(maxDays > 0)

        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private fun makeFile(): File {
        return File(dir, "$baseName.$extName")
    }

    private fun makeBackupFile(): File {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val ds = fmt.format(Date(System.currentTimeMillis()))
        val reg: Regex = Regex("${baseName}_$ds\\.(\\d+)\\.$extName")
        var idx: Int = 0
        val fileList: List<File> = dir.listFiles()?.toList() ?: emptyList()
        for (f: File in fileList) {
            val m = reg.matchEntire(f.name)
            if (m != null) {
                val n = m.groupValues.getOrNull(1)?.toInt() ?: 0
                if (n > idx) idx = n
            }
        }
        idx += 1
        var f = File(dir, "${baseName}_$ds.$idx.$extName")
        while (f.exists()) {
            idx += 1
            f = File(dir, "${baseName}_$ds.$idx.$extName")
        }
        return f
    }

    @Synchronized
    override fun dispose() {
        lines = 0
        filePrinter?.dispose()
        filePrinter = null;
    }

    @Synchronized
    override fun flush() {
        filePrinter?.flush()
    }

    @Synchronized
    override fun printItem(item: LogItem) {
        lines += 1
        checkFile()
        filePrinter?.printItem(item)
    }

    @Suppress("UNUSED_PARAMETER")
    @Synchronized
    private fun checkFile() {

        if (filePrinter == null) {
            createNewFile()
            return
        }
        if (lines > MIN_LINES) {
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

    private fun backup(file: File) {

    }

    private fun createNewFile() {
        dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        lines = 0
        val oldFile: File? = filePrinter?.file
        filePrinter?.dispose()
        filePrinter = null
        oldFile?.also { it.renameTo(makeBackupFile()) }
        filePrinter = FilePrinter(makeFile())
        Thread { deleteExpired() }.start()
    }

    private fun deleteExpired() {
        if (maxDays <= 0) {
            return
        }
        val fs = dir.listFiles()?.filter { it.name.matches(nameRegex) }?.sortedBy { it.lastModified() } ?: return

        val c: Calendar = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, -maxDays)
        val tm: Long = c.timeInMillis
        val minIndex = fs.size - MAX_COUNT
        for (i in fs.indices) {
            val f = fs[i]
            if (i < minIndex) {
                f.delete()
            } else if (f.lastModified() < tm) {
                f.delete()
            } else {
                break
            }
        }
    }
}