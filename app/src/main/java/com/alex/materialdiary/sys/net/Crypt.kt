package com.alex.materialdiary.sys.net

import android.util.Base64
import java.util.Random

public class Crypt {
    var real_key = "com.alex.materialdiary"
    infix fun String.xor(that: String) = mapIndexed { index, c ->
        that[index].code.xor(c.code)
    }.joinToString(separator = "") {
        it.toChar().toString()
    }

    init {
        val key = "cnUuaW50ZWdyaWNzLm1vYmlsZXNjaG9vbA=="
        real_key = String(Base64.decode(key, Base64.NO_WRAP))
    }

    fun encryptSYS_GUID(paramString: String): String {
        return Base64.encodeToString(
            (paramString.substring(0, paramString.length / 2)).xor(real_key).toByteArray(),
            Base64.NO_WRAP
        )
    }

    companion object {
        fun randomSymbols(i: Int): String {
            val random = Random()
            val sb = StringBuilder(i)
            for (i2 in 0 until i) {
                sb.append("0123456789qwertyuiopasdfghjklzxcvbnm"[random.nextInt(36)])
            }
            return sb.toString()
        }

        public fun genPdaKey(): String {
            val l = java.lang.Long.valueOf(System.currentTimeMillis() / 1000).toString()
            return l + "-" + randomSymbols(8)
        }
    }
}