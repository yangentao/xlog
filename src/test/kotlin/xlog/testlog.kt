package xlog

import io.github.yangentao.xlog.*
import java.io.File

fun main() {
    val tp = TreePrinter()
    tp.add(ConsolePrinter)
    tp.add(DirPrinter(File("/Users/entao/Downloads/a"), fileSizeM = 1))
    tp.add(DirPrinter(File("/Users/entao/Downloads/a"), fileSizeM = 1, namePrefix = "err-"), LevelFilter(LogLevel.ERROR))
    XLog.setPrinter(tp, LevelFilter(LogLevel.INFO))
    var i = 1
    while (i < 100) {
        val n = i % 5
        when (n) {
            0 -> logv("Hello $i")
            1 -> logd("Hello $i")
            2 -> logi("Hello $i")
            3 -> logw("Hello $i")
            4 -> loge("Hello $i")
        }
        i += 1
        Thread.sleep(100)
    }
}