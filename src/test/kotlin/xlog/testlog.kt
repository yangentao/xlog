package xlog

import io.github.yangentao.xlog.*
import java.io.File

fun main() {
    val tp = TreePrinter()
    tp.add(ConsolePrinter)
    tp.add(DirPrinter(File("/Users/entao/Downloads/a"), fileSize = 10_000))
    tp.add(DirPrinter(File("/Users/entao/Downloads/a"), fileSize = 10_000, baseName = "err", checkLineCount = 100), LevelFilter(LogLevel.ERROR))
    XLog.setPrinter(tp, LevelFilter(LogLevel.ALL))
    var i = 1
    while (i < 1000) {
        val n = i % 5
        when (n) {
            0 -> logv("Hello $i")
            1 -> logd("Hello $i")
            2 -> logi("Hello $i")
            3 -> logw("Hello $i")
            4 -> loge("Hello $i")
        }
        loge("this is error $i")
        i += 1
    }
}