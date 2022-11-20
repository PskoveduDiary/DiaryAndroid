package com.alex.materialdiary.sys.messages;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.ListPreference;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alex.materialdiary.R;

import org.json.JSONObject;

import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotificationService extends Service implements API.Callback_Contacts {
    public Handler mHandler;
    public Runnable mRunnable;

    NotificationManagerCompat notificationManager;
    NotificationManager nM;
    public API api;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        mHandler.postDelayed(mRunnable, 60000);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(CookieManager.getInstance().getCookie("one.pskovedu.ru") != "") {
            api = new API(CookieManager.getInstance().getCookie("one.pskovedu.ru"));
        }
        else {
            api = null;
        }
        String cm = CookieManager.getInstance().getCookie("one.pskovedu.ru");
        if (cm == null) cm = "No";
        Log.d("not", String.valueOf(cm));
        notificationManager = NotificationManagerCompat.from(this);
        nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mHandler = new Handler();
        mRunnable = this::CheckMessages;
        if (!cm.equals("No")) mHandler.postDelayed(mRunnable, 30000);
        return Service.START_STICKY;
    }
    public void CheckMessages(){
        if (api != null) api.GetContacts(this);
        mHandler.postDelayed(mRunnable, 90000);
    }

    @Override
    public void Contacts(List<String> logins, List<String> names, List<Integer> unreaded, List<Boolean> isGroup) {
        List<Integer> sended = new ArrayList<Integer>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StatusBarNotification[] n = nM.getActiveNotifications();

            for (StatusBarNotification statusBarNotification : n) {
                sended.add(statusBarNotification.getId());
            }
        }
        for (int i = 0; i<logins.size(); i++){
            if (unreaded.get(i) > 0){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "msg")
                        .setSmallIcon(R.drawable.ic_baseline_message_24)
                        .setContentTitle("Новое сообщение")
                        .setContentText(names.get(i) + " прислал(а) вам сообщение")
                        .setSubText(String.valueOf(unreaded.get(i)) + " шт.")
                        .setNumber(unreaded.get(i))
                        .setOnlyAlertOnce(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(names.get(i) + " прислал(а) вам сообщение"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setNumber(unreaded.get(i))
                        .setAutoCancel(true);
                if (!sended.contains(logins.get(i).hashCode())) notificationManager.notify(logins.get(i).hashCode(), builder.build());
            }
        }
    }

    @Override
    public void NeedRestart() {

    }
}