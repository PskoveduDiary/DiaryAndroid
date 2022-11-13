package com.alex.materialdiary.sys.common;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {
  private static Cipher cipher;
  
  private static SecretKeySpec key;
  
  private static String transformation = "AES/ECB/PKCS5Padding";
  
  //private static SecretKeySpec createSecretKey() {
  //  byte[] arrayOfByte = new byte[16];
  //  (new SecureRandom()).nextBytes(arrayOfByte);
  //  try {
  //    byte[] arrayOfByte1 = Arrays.copyOf(MessageDigest.getInstance("SHA-1").digest(arrayOfByte), 16);
  //    SecretKeySpec secretKeySpec = new SecretKeySpec(arrayOfByte1, "AES");
  //    return secretKeySpec;
  //  } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
  //    noSuchAlgorithmException = null;
  //    return null;
  //  }
  //}
  
  private static byte[] decrypt(SecretKeySpec paramSecretKeySpec, byte[] paramArrayOfbyte) {
    try {
      cipher = Cipher.getInstance(transformation);
      cipher.init(Cipher.DECRYPT_MODE, paramSecretKeySpec);
      return cipher.doFinal(paramArrayOfbyte);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static String decryptSySGUID(String paramString) {
    byte[] arrayOfByte = decrypt(key, Base64.decode(paramString, 0));
    return (arrayOfByte != null) ? new String(arrayOfByte) : "";
  }
  
  private static byte[] encrypt(SecretKeySpec paramSecretKeySpec, byte[] paramArrayOfbyte) {
    try {
      cipher = Cipher.getInstance(transformation);
      cipher.init(Cipher.ENCRYPT_MODE, paramSecretKeySpec);
      return cipher.doFinal(paramArrayOfbyte);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public static String encryptSYS_GUID(String paramString) {
    return Base64.encodeToString(encrypt(key, paramString.getBytes()), Base64.NO_WRAP);
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
  
  public static void generateKeyFromString(String paramString) {
    byte[] arrayOfByte = Base64.decode(paramString, 0);
    key = new SecretKeySpec(arrayOfByte, 0, arrayOfByte.length, "AES");
  }
}
