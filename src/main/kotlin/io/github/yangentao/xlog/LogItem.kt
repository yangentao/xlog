package io.github.yangentao.xlog

data class LogItem(val level: LogLevel, val tag: String, val message: String, val tid: Long, val tm: Long = System.currentTimeMillis()) {

    private val foramttedMessage: String by lazy { XLog.formatMessage(this) }

    override fun toString(): String {
        return foramttedMessage
    }
}
