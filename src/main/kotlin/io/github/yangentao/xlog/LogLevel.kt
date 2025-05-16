package io.github.yangentao.xlog

@Suppress("unused")
enum class LogLevel(val value: Int) {
    ALL(0), VERBOSE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FATAIL(6), OFF(9);

    fun ge(other: LogLevel): Boolean {
        return this.value >= other.value
    }
}