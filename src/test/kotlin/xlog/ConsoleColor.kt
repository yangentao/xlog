package xlog

import io.github.yangentao.xlog.*
import kotlin.test.Test

class ConsoleColor {
    @Test
    fun color() {
        println("Start\u001b[31m Hello \u001b[0mEnd")
        println(CSI("Hello").invert.text + CSI("Hello").bold.red.text + " Yang")
        println(CSI("Hello").fore(208).back(51).text)
        logv("this is an verbose")
        logd("this is an debug")
        logi("this is an info")
        logw("this is an warning")
        loge("this is an error")
    }
}