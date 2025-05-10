package io.github.yangentao.xlog

class TreePrinter(vararg ps: Pair<LogPrinter, LogFilter>) : LogPrinter {
    private val list: ArrayList<Pair<LogPrinter, LogFilter>> = arrayListOf(*ps)
    override fun printItem(item: LogItem) {
        for (p in list) {
            if (p.second.accept(item)) {
                p.first.printItem(item)
            }
        }
    }

    fun add(p: LogPrinter, f: LogFilter = LevelFilter(LogLevel.ALL)) {
        list.add(p to f)
    }

    fun remove(p: LogPrinter) {
        list.removeIf { it.first == p }
        p.dispose()
    }

    override fun flush() {
        for (p in list) {
            p.first.flush()
        }
    }

    override fun dispose() {
        for (p in list) {
            p.first.flush()
            p.first.dispose()
        }
        list.clear()
    }

}