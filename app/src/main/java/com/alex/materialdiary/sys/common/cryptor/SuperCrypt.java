package com.alex.materialdiary.sys.common.cryptor;
//com/alex/materialdiary/sys/common/cryptor/SuperCrypt

import static xdroid.toaster.Toaster.toast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.Signature;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.security.cert.CertificateException;


public class SuperCrypt {
    public static Context context;

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
    }

    public SuperCrypt(){
        System.loadLibrary("adlemx");
        toast(cry("dddddd"));
    }
    public native String cry(String str);

    public PackageManager getPackageManager(){
        return context.getPackageManager();
    }
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
