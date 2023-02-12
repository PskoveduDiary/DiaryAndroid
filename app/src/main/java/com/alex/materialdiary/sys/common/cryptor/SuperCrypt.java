package com.alex.materialdiary.sys.common.cryptor;
//com/alex/materialdiary/sys/common/cryptor/SuperCrypt

import static xdroid.toaster.Toaster.toast;

import android.content.Context;


public class SuperCrypt {
    /*public static Context context;

    public static void setContext(Context context) {
        SuperCrypt.context = context;
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }*/

    public SuperCrypt(){
        System.loadLibrary("adlemx");
    }
    public native String makeBlackMagic(String str);

    //public PackageManager getPackageManager(){
    //    return context.getPackageManager();
    //}
    /*public String getPackageName(){
        return "ru.integrics.mobileschool";
    }*/
/*
    public String get(String str){
        Log.e("my vk/tg", "@dianasilna");
        toast(cry(str.substring(0, str.length() / 2)));
        toast(cry(str));
        String key = Base64.encodeToString(cry(str.substring(0, str.length() / 2)).getBytes(), Base64.NO_WRAP);
        return key;
    }*/

}
