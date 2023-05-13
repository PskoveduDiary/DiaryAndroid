package com.alex.materialdiary.widgets

import android.content.Intent
import android.widget.RemoteViewsService.RemoteViewsFactory
import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.alex.materialdiary.R
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.ArrayList

class LessonsFactory internal constructor(var context: Context, intent: Intent) :
    RemoteViewsFactory {
    var data: ArrayList<String>? = null
    var sdf: SimpleDateFormat
    var widgetID: Int

    init {
        sdf = SimpleDateFormat("HH:mm:ss")
        widgetID = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    override fun onCreate() {
        data = ArrayList()
    }

    override fun getCount(): Int {
        return data!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewAt(position: Int): RemoteViews? {
        //val rView = RemoteViews(
        //    context.packageName,
        //    R.layout.lesson_item
        //)
        //rView.setTextViewText(R.id.tvItemText, data!![position])
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        data!!.clear()
        data!!.add(sdf.format(Date(System.currentTimeMillis())))
        data!!.add(hashCode().toString())
        data!!.add(widgetID.toString())
        for (i in 3..14) {
            data!!.add("Item $i")
        }
    }

    override fun onDestroy() {}
}