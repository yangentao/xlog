package io.github.yangentao.xlog

interface LogFilter {
    fun accept(item: LogItem): Boolean
}

class LevelFilter(val level: LogLevel) : LogFilter {
    override fun accept(item: LogItem): Boolean {
        return item.level.ge(level)
    }

}

class DenyTagFilter(val tags: Set<String>) : LogFilter {
    override fun accept(item: LogItem): Boolean {
        return !tags.contains(item.tag)
    }
}

class OnlyTagFilter(val tags: Set<String>) : LogFilter {
    override fun accept(item: LogItem): Boolean {
        return tags.contains(item.tag)
    }
}

class TreeLogFilter(val filters: List<LogFilter>) : LogFilter {
    override fun accept(item: LogItem): Boolean {
        return filters.all { it.accept(item) }
    }
}