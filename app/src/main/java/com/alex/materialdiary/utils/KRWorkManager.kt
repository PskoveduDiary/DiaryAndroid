package com.alex.materialdiary.utils

import android.R
import android.content.Context
import androidx.work.*
import com.alex.materialdiary.workers.KRNotifyWorker
import org.joda.time.DateTime
import org.joda.time.Duration
import xdroid.toaster.Toaster.toast
import java.util.concurrent.TimeUnit

class KRWorkManager {
    val SELF_REMINDER_HOUR = 21
    fun start(context: Context, time: Int = SELF_REMINDER_HOUR){
        val delay = when (DateTime.now().hourOfDay < time) {
            true -> Duration(
                DateTime.now(),
                DateTime.now().withTimeAtStartOfDay().plusHours(time)
            ).getStandardMinutes()
            else ->
            Duration(
                DateTime.now(),
                DateTime.now().withTimeAtStartOfDay().plusHours(time).plusDays(1)
            ).getStandardMinutes()
        }

        /*val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .build()*/
        toast(delay.toString())
        val workRequest = PeriodicWorkRequest.Builder(
            KRNotifyWorker::class.java,
            15,
            TimeUnit.MINUTES
        )
            //.setConstraints(Constraints(constraints))
            .setInitialDelay(delay.toLong(), TimeUnit.MINUTES)
            .addTag("kr_notify")
            .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "kr_notify",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }
    fun stop(context: Context){
        WorkManager.getInstance(context).cancelAllWorkByTag("kr_notify")
    }
}