package com.alex.materialdiary.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.R
import com.alex.materialdiary.keywords
import com.alex.materialdiary.sys.common.CommonAPI
import com.alex.materialdiary.sys.common.Crypt
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService(), CommonAPI.CommonCallback {

    private lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //if (remoteMessage.data.get("type") == "kr_test"){
        //    //Crypt.generateKeyFromString("aYXfLjOMB9V5az9Ce8l+7A==");
        //    val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
        //    val api = CommonAPI(baseContext)
        //    api.getDay(this, cuurent_date.toString())
        //}
        sendNotification(remoteMessage)
        if (remoteMessage.data.get("type") == "kr"){
            //Crypt.generateKeyFromString("aYXfLjOMB9V5az9Ce8l+7A==");
            val cuurent_date = Date(Calendar.getInstance().time.time + 86400000)
            val api = CommonAPI(baseContext)
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
        if (lessns.size > 0) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigate", "kr")
            val pendingIntent = PendingIntent.getActivity(
                this,
                123, intent, PendingIntent.FLAG_IMMUTABLE
            )
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(baseContext, "kr")
                .setSmallIcon(R.drawable.ic_baseline_error_outline_24)
                .setContentTitle("Контрольные!")
                .setContentText(
                    "Подготовься, завтра могут быть контрольные по ${
                        baseContext.resources.getQuantityString(
                            R.plurals.kr,
                            no_dubls.size,
                            no_dubls.size
                        )
                    }"
                )
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            notificationManager.notify(1233, builder.build())
        }
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
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigate", "kr")
        val pendingIntent = PendingIntent.getActivity(
            this,
            123, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "adsad"
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder  = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentText(remoteMessage.data.get("kr"))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        // Необходим канал уведомлений Android Oreo
        // Необходим канал уведомлений Android Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(123, notificationBuilder.build())
    }


}