package io.github.yangentao.xlog

import java.text.SimpleDateFormat
import java.util.*

interface LogFormatter {
    fun format(item: LogItem): String
}

object DefaultLogFormatter : LogFormatter {
    override fun format(item: LogItem): String {
        val sb = StringBuilder(item.message.length + 64)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date(item.tm))
        sb.append(date).append(' ')
        sb.append(item.level.name.first()).append(' ')
//        sb.append(String.format(Locale.getDefault(), " [%4d] ", item.tid))
        sb.append(item.tid.toString()).append(' ')
        sb.append(item.tag)
        sb.append(": ")
        sb.append(item.message)
        return sb.toString()
    }
}