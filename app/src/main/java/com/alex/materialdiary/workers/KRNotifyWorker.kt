package com.alex.materialdiary.workers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alex.materialdiary.R
import com.alex.materialdiary.keywords
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.utils.KRWorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class KRNotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    val context = appContext
    val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.

        val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
        getDay(cuurent_date.toString())
        /*val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "kr")
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle("Сработало!")
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        notificationManager.notify(1233, builder.build())*/
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
    fun getDay(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val lesson = PskoveduApi.getInstance(context).getDay(date)?.data ?: return@launch
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
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_DENIED
            ) notificationManager.notify(69, builder.build())
        }
    }

    override fun onStopped() {
        super.onStopped()
        KRWorkManager().start(context)
    }
}