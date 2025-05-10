package xlog

import io.github.yangentao.xlog.DirPrinter
import io.github.yangentao.xlog.XLog
import io.github.yangentao.xlog.logd
import java.io.File

fun main() {
    XLog.setPrinter(DirPrinter(File("/Users/entao/Downloads/a"), fileSizeM = 1))
    var i = 1
    while (i < 20) {
        logd("Hello $i")
        i += 1
        Thread.sleep(1000)
    }
}