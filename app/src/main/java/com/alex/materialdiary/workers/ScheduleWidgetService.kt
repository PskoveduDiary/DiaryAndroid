package com.alex.materialdiary.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.R
import com.alex.materialdiary.sys.net.PskoveduApi
import com.alex.materialdiary.sys.net.models.diary_day.DatumDay
import kotlinx.coroutines.runBlocking

class ScheduleWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return SheduleRemoteViewsFactory(applicationContext)
    }

    class SheduleRemoteViewsFactory(context: Context) : RemoteViewsFactory {
        private val context: Context

        //private val reminderRepository: ReminderRepository
        private var schedule_data: MutableList<DatumDay> = mutableListOf()

        init {
            this.context = context
            //reminderRepository = ReminderRepository.getReminderRepository(context)
        }

        override fun onCreate() {

            //reminders = mutableListOf("Урок 1", "Урок 2", "Урок 3")//reminderRepository.getAllRemindersForTodayList()
        }

        override fun onDataSetChanged() {
            runBlocking {
                val data = PskoveduApi.getInstance(context).getDay()?.data
                schedule_data = data!!
            }

        }

        override fun onDestroy() {
            schedule_data.clear()
        }

        override fun getCount(): Int {
            return schedule_data.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val lesson = schedule_data[position]
            val remoteView = RemoteViews(context.packageName, R.layout.shedule_item)
            remoteView.setTextViewText(R.id.subjName, lesson.subjectName)
            remoteView.setTextViewText(R.id.startTime, lesson.lessonTimeBegin)
            remoteView.setTextViewText(R.id.endTime, lesson.lessonTimeEnd)
            remoteView.setTextViewText(R.id.endTime, lesson.lessonTimeEnd)
            remoteView.setTextViewText(R.id.teacherName, lesson.teacherName)
            remoteView.setOnClickFillInIntent(R.id.sheduleListItem, Intent().putExtra("navigate", "diary"))
            // Removed code that sets the other fields as I tried it with and without and it didn't matter. So removed for brevity
            return remoteView
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return schedule_data[position].hashCode().toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }
}