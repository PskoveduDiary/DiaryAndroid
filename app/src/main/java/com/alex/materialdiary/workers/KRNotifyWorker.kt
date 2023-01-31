package com.alex.materialdiary.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alex.materialdiary.R
import com.alex.materialdiary.keywords
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import com.alex.materialdiary.utils.KRWorkManager
import xdroid.toaster.Toaster.toast
import java.util.*

class KRNotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), CommonAPI.CommonCallback {
    val context = appContext
    val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.

        val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
        val api = CommonAPI(context)
        api.getDay(this, cuurent_date.toString())
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "kr")
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle("Сработало!")
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        notificationManager.notify(1233, builder.build())
        return Result.success()
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
        if (lessns.size > 0) {
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
            notificationManager.notify(1233, builder.build())
        }
    }

    override fun onStopped() {
        super.onStopped()
        KRWorkManager().start(context)
    }
}