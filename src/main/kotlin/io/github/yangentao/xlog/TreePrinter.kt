package io.github.yangentao.xlog

class TreePrinter(vararg ps: LogPrinter) : LogPrinter {
    private val list: ArrayList<LogPrinter> = arrayListOf(*ps)
    override fun printItem(item: LogItem) {
        for (p in list) {
            p.printItem(item)
        }
    }

    fun add(p: LogPrinter) {
        list.add(p)
    }

    fun remove(p: LogPrinter) {
        list.remove(p)
        p.dispose()
    }

    override fun flush() {
        for (p in list) {
            p.flush()
        }
    }

    override fun dispose() {
        for (p in list) {
            p.flush()
            p.dispose()
        }
        list.clear()
    }

}