package com.alex.materialdiary.utils

import android.content.Context
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriodData
import com.alex.materialdiary.sys.common.models.marks.Item
import com.alex.materialdiary.sys.common.models.marks.Mark
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter


class MarksTranslator(val pm: MutableList<PeriodMarksData>) {
    val items: MutableList<Item> = mutableListOf()
    init {
        pm.forEach {
            val marks: MutableList<Mark> = mutableListOf()
            it.marks.forEach { mark ->
            marks += Mark(mark.value, mark.date)
            }
            items += Item(marks, it.subjectName)
        }
    }
    companion object{

        fun getSubjectMarksDifferences(context: Context, subj_name: String?, neww: List<Item>): List<Mark?>? {
            val utils = ReadWriteJsonFileUtils(context)
            val readed = utils.readJsonFileData("marks.json")
                ?: return ArrayList()
            val listType = object : TypeToken<ArrayList<Item?>?>() {}.type
            val old = Gson().fromJson<List<Item>>(readed, listType)
            val old_item = old.find { it.name == subj_name } ?: return ArrayList()
            val new_item = neww.find { it.name == subj_name } ?: return ArrayList()
            val diff: MutableList<Mark?> = ArrayList(
                new_item.marks
            )
            diff.removeAll(old_item.marks)
            return diff
        }
        fun get_cur_period(periods: List<AllPeriodData>): MutableList<LocalDate> {
            val formatter: DateTimeFormatter? = DateTimeFormat.forPattern("dd.MM.yyyy")
            val datestr = mutableListOf<MutableList<String>>()
            periods.forEach { datestr += mutableListOf(it.dateBegin, it.dateEnd) }
            val dates = mutableListOf<MutableList<LocalDate>>()
            datestr.forEach {
                val thi = mutableListOf<LocalDate>()
                it.forEach { thi += LocalDate.parse(it, formatter) }
                dates += thi
            }
            dates.forEach {
                if (LocalDate.now() in it[0]..it[1])  {
                    return it
                }
            }
            return mutableListOf()
        }
    }
}