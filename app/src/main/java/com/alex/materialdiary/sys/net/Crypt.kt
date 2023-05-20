package com.alex.materialdiary.sys.net

import android.util.Base64

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
}