package com.alex.materialdiary.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.adapters.ProgramAdapterDiary
import com.alex.materialdiary.sys.common.CommonService
import com.alex.materialdiary.sys.common.Crypt
import com.alex.materialdiary.sys.common.PskoveduApi
import com.alex.materialdiary.sys.common.models.ClassicBody
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Implementation of App Widget functionality.
 */
class DiaryWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName,
        R.layout.diary_widget)
    val marksJob = CoroutineScope(Dispatchers.IO).launch {
        try {
            val api = PskoveduApi.getInstance(context)
            val lessons = api.getDay()
            withContext(Dispatchers.Main) {
                //views.setRemoteAdapter(R.id.lessons, Intent(context, ProgramAdapterDiary.class, ""))
            }
        }  catch (e: Exception) {
        }
    }
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}