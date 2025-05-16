package io.github.yangentao.xlog

import java.io.PrintWriter
import java.io.StringWriter

internal fun logArrayToString(args: Array<out Any?>): String {
    return args.joinToString(" ") {
        anyToString(it)
    }
}

internal fun anyToString(obj: Any?): String {
    if (obj == null) {
        return "null"
    }
    if (obj is String) {
        return obj
    }
    if (obj.javaClass.isPrimitive) {
        return obj.toString()
    }

    if (obj is Throwable) {
        val sw = StringWriter(512)
        val pw = PrintWriter(sw)
        obj.printStackTrace(pw)
        return sw.toString()
    }

    return when (obj) {
        is Array<*> -> "Array[${obj.joinToString(",") { anyToString(it) }}]"
        is List<*> -> "List[${obj.joinToString(",") { anyToString(it) }}]"
        is Set<*> -> "Set[${obj.joinToString(",") { anyToString(it) }}]"
        is Map<*, *> -> "Map[${obj.map { "${anyToString(it.key)} = ${anyToString(it.value)}" }.joinToString(",")}}]"
        is Iterable<*> -> "Iterable[${obj.joinToString(",") { anyToString(it) }}]"
        is ByteArray -> "ByteArray[${obj.joinToString { hexByte(it.toInt()) }}]"
        else -> {
            if (obj::class.java.isArray) {
                val length: Int = java.lang.reflect.Array.getLength(obj)
                val ls = ArrayList<String>()
                for (i in 0..<length) {
                    val v = java.lang.reflect.Array.get(obj, i)
                    ls.add(anyToString(v))
                }
                "[" + ls.joinToString(",") + "]"
            } else {
                obj.toString()
            }
        }
    }
}

private val DICT = "0123456789ABCDEF"
private fun hexByte(b: Int): String {
    return String(charArrayOf(DICT[(b shr 4) and 0xf], DICT[b and 0xf]))
}

