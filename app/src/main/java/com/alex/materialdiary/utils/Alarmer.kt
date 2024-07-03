package com.alex.materialdiary.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequest
import com.alex.materialdiary.workers.AlarmKrReceiver
import xdroid.toaster.Toaster.toast
import java.util.*


class Alarmer {
    fun start_kr(context: Context){
        /*var alarmMgr: AlarmManager? = null
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmKrReceiver::class.java)
        var alarmIntent: PendingIntent = intent.let {
            PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_IMMUTABLE)

        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 17)
        }
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            alarmIntent
        )*/
    }
}