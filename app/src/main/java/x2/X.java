package x2;

import android.os.Build;

import java.util.Base64;

/* renamed from: x2.X */
/* loaded from: classes.dex */
public class X {
        static {
            System.loadLibrary("adlemx");
        }

        /* renamed from: do */
        public static String m0do(String str) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(x01(str).getBytes());
            }
            return "";
        }

        public static native String x01(String str);
    }
