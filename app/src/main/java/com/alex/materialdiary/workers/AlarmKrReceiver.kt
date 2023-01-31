package com.alex.materialdiary.workers

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alex.materialdiary.R
import com.alex.materialdiary.keywords
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import java.util.*

class AlarmKrReceiver : BroadcastReceiver(), CommonAPI.CommonCallback {

    private lateinit var nm: NotificationManagerCompat
    lateinit var context: Context

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(context: Context) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "kr")
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle("Будильник сработал!")
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        nm.notify(21231, builder.build())
    }
    fun check(str: String): MutableList<String> {
        val finded = mutableListOf<String>()
        keywords.kr_mini.forEach {
            if (str.contains(it)) {
                finded.add(it)
            }
        }
        keywords.kr_maybe.forEach {
            if (str.contains(it)) {
                finded.add(it)
            }
        }
        keywords.kr.forEach {
            if (str.contains(it)) {
                finded.add(it)
            }
        }
        return finded
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            this.context = context
            nm = NotificationManagerCompat.from(context)
            val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
            val api = CommonAPI(context)
            api.getDay(this, cuurent_date.toString())
        }
    }

    override fun day(lesson: MutableList<DatumDay>?) {
            if (lesson == null) return
            val lessns = mutableListOf<String>()
            for (lsn in lesson) {
                val finded = mutableListOf<String>()
                if (lsn.homeworkPrevious?.homework != null) {
                    val c = lsn.homeworkPrevious!!.homework!!
                    finded += check(c)
                }
                if (lsn.topic != null) {
                    finded += check(lsn.topic!!)
                }
                if (finded.size > 0){
                    lsn.subjectName?.let { lessns.add(it) }
                }
            }
            val no_dubls = lessns.distinct()
            /*if (lessns.size > 0) {*/
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "kr")
                    .setSmallIcon(R.drawable.ic_baseline_error_outline_24)
                    .setContentTitle("Контрольные!")
                    .setContentText(
                        "Подготовься, завтра могут быть контрольные по ${
                            context.resources.getQuantityString(
                                R.plurals.kr,
                                no_dubls.size,
                                no_dubls.size
                            )
                        }"
                    )
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                nm.notify(1233, builder.build())
            /*}*/
        }
}