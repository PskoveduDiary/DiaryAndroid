package com.alex.materialdiary.sys.common
/*
import android.util.Base64
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Crypt {
    private var cipher: Cipher? = null
    private var key: SecretKeySpec? = null
    private const val transformation = "AES/ECB/PKCS5Padding"


    private fun decrypt(
        paramSecretKeySpec: SecretKeySpec?,
        paramArrayOfbyte: ByteArray
    ): ByteArray? {
        return try {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.DECRYPT_MODE, paramSecretKeySpec)
            cipher.doFinal(paramArrayOfbyte)
        } catch (exception: Exception) {
            null
        }
    }

    fun decryptSySGUID(paramString: String?): String {
        val arrayOfByte = decrypt(key, Base64.decode(paramString, 0))
        return arrayOfByte?.let { String(it) } ?: ""
    }

    private fun encrypt(
        paramSecretKeySpec: SecretKeySpec?,
        paramArrayOfbyte: ByteArray
    ): ByteArray? {
        return try {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, paramSecretKeySpec)
            cipher.doFinal(paramArrayOfbyte)
        } catch (exception: Exception) {
            null
        }
    }

    @JvmStatic
    fun encryptSYS_GUID(paramString: String): String {
        return Base64.encodeToString(encrypt(key, paramString.toByteArray()), Base64.NO_WRAP)
    }

    //public static void generateKey() {
    //  key = createSecretKey();
    //  String str = Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    //  StringBuilder stringBuilder = new StringBuilder();
    //  stringBuilder.append("generated key_string:'");
    //  stringBuilder.append(str);
    //  stringBuilder.append("'");
    //  Log.d("crypt", stringBuilder.toString());
    //}
    @JvmStatic
    fun generateKeyFromString(str: String?) {
        val decode = Base64.decode(str, 0)
        key = SecretKeySpec(decode, 0, decode.size, "AES")
    }
}*/