package com.alex.materialdiary

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.alex.materialdiary.workers.ScheduleWidgetService


/**
 * Implementation of App Widget functionality.
 */
class SheduleWidget : AppWidgetProvider() {
    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val intent = Intent(context, ScheduleWidgetService::class.java)
        val remoteViews = RemoteViews(context.packageName, R.layout.shedule_widget)
        remoteViews.setRemoteAdapter(R.id.sheduleListView, intent)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.sheduleListView)
        remoteViews.setEmptyView(R.id.sheduleListView, R.id.widgetEmptyViewText)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */  0,
            /* intent = */ Intent(context, MainActivity::class.java),
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */  1,
            /* intent = */ Intent(context, MainActivity::class.java).putExtra("navigate", "diary"),
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        remoteViews.setOnClickPendingIntent(R.id.widgetEmptyViewText, pendingIntent)
        remoteViews.setPendingIntentTemplate(R.id.sheduleListView, pendingIntent2)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    /*val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.shedule_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)*/
}