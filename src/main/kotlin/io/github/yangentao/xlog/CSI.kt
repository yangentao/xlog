package io.github.yangentao.xlog

val String.csi: CSI get() = CSI(this)

/// https://en.wikipedia.org/wiki/ANSI_escape_code#Colors
@Suppress("unused")
class CSI(private val str: String) {
    private val sgrList: ArrayList<String> = ArrayList()

    val text: String
        get() {
            return sgrList.joinToString("") + str + "\u001b[0m"
        }

    fun fore(code: Int): CSI {
        sgrList.add("\u001b[38:5:${code}m")
        return this
    }

    fun back(code: Int): CSI {
        sgrList.add("\u001b[48:5:${code}m")
        return this
    }

    val black: CSI get() = sgr(30)
    val red: CSI get() = sgr(31)
    val green: CSI get() = sgr(32)
    val yellow: CSI get() = sgr(33)
    val blue: CSI get() = sgr(34)
    val magenta: CSI get() = sgr(35)
    val cyan: CSI get() = sgr(36)
    val white: CSI get() = sgr(37)
    val backBlack: CSI get() = sgr(40)
    val backRed: CSI get() = sgr(41)
    val backGreen: CSI get() = sgr(42)
    val backYellow: CSI get() = sgr(43)
    val backBlue: CSI get() = sgr(44)
    val backMagenta: CSI get() = sgr(45)
    val backCyan: CSI get() = sgr(46)
    val backWhite: CSI get() = sgr(47)

    val invert: CSI get() = sgr(7)
    val bold: CSI get() = sgr(1)
    val faint: CSI get() = sgr(2)
    val italic: CSI get() = sgr(3)
    val underline: CSI get() = sgr(4)

    fun sgr(code: Int): CSI {
        sgrList.add("\u001b[${code}m")
        return this
    }

    override fun toString(): String {
        return text
    }
}