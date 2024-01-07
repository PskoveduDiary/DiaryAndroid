package com.alex.materialdiary

/**
 * Application class to enable immediate access to module contents.
 * This can be done in multiple ways.
 * See https://developer.android.com/guide/playcore#access_downloaded_modules for more guidance.
 */
import android.content.Intent
import android.util.Log
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.alex.materialdiary.sys.net.database.CacheDatabase
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import kotlin.system.exitProcess


/**
 * Application class to enable immediate access to module contents.
 * This can be done in multiple ways.
 * See https://developer.android.com/guide/playcore#access_downloaded_modules for more guidance.
 */
class MyApplication : SplitCompatApplication() {
    private var db: CacheDatabase? = null

    companion object {
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            FirebaseCrashlytics.getInstance().recordException(e)
            Log.e("crash", Log.getStackTraceString(e))
            val i = Intent(applicationContext, FatalErrorActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)

            exitProcess(1)
        }
        db = databaseBuilder(this, CacheDatabase::class.java, "cache.db")
            .build()
        db!!.openHelper.writableDatabase
        CoroutineScope(Dispatchers.IO).launch {
            db!!.cacheDao()
                .flushOldMarksRecords(
                    DateTime.now().minusDays(7).millis
                )
            db!!.cacheDao()
                .flushOldRecords(
                    DateTime.now().minusMonths(3).millis
                )
        }

    }

    fun getDb(): CacheDatabase? = db
}