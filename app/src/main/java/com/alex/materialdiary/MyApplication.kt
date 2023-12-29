package com.alex.materialdiary

/**
 * Application class to enable immediate access to module contents.
 * This can be done in multiple ways.
 * See https://developer.android.com/guide/playcore#access_downloaded_modules for more guidance.
 */
import android.content.Intent
import android.util.Log
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlin.system.exitProcess


/**
 * Application class to enable immediate access to module contents.
 * This can be done in multiple ways.
 * See https://developer.android.com/guide/playcore#access_downloaded_modules for more guidance.
 */
class MyApplication : SplitCompatApplication(){
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            FirebaseCrashlytics.getInstance().recordException(e)
            Log.e("crash", Log.getStackTraceString(e))
            val i = Intent(applicationContext, FatalErrorActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)

            exitProcess(1)
        }
    }
}