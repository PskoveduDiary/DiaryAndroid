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

class MarksNotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    val context = appContext
    val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.

        check()
        /*val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "kr")
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle("Сработало!")
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        notificationManager.notify(1233, builder.build())*/
        return Result.success()
    }

    fun check() {
        CoroutineScope(Dispatchers.IO).launch {
            var newMarksCount = 0
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "marks")
                .setSmallIcon(R.drawable.ic_baseline_looks_5_24)
                .setContentTitle("Новые оценки")
                .setContentText(
                    context.resources.getQuantityString(
                        R.plurals.marks,
                        newMarksCount,
                        newMarksCount
                    )
                )
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_DENIED
            ) notificationManager.notify(7, builder.build())
        }
    }

    override fun onStopped() {
        super.onStopped()
        KRWorkManager().start(context)
    }
}