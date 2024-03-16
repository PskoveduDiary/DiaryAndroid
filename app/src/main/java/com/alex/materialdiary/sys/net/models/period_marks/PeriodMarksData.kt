package com.alex.materialdiary.sys.net.models.period_marks

import android.content.Context
import com.alex.materialdiary.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.DecimalFormat

data class PeriodMarksData(
    @SerializedName("JOURNAL_SYS_GUID")
    var journalSysGuid: String? = null,

    @SerializedName("JOURNAL_NAME")
    var journalName: String? = null,

    @SerializedName("SUBJECT_SYS_GUID")
    var subjectSysGuid: String? = null,

    @SerializedName("SUBJECT_NAME")
    var subjectName: String,

    @SerializedName("TEACHER_SYS_GUID")
    var teacherSysGuid: String? = null,

    @SerializedName("TEACHER_NAME")
    var teacherName: String? = null,

    @SerializedName("TEACHER_NAME_SHORT")
    var teacherNameShort: String? = null,

    @SerializedName("HIDE_IN_REPORTS")
    var hideInReports: Boolean? = null,

    @SerializedName("HIDE_IN_SCHEDULE")
    var hideInSchedule: Boolean? = null,

    @SerializedName("MARKS")
    var marks: List<Mark>
) : Serializable {
    val average: Double
        get() {
            if (marks.size == 0) return -100.0
            var com = 0
            for (i in marks.indices) {
                com += marks[i].value
            }
            return (com.toFloat() / marks.size).toDouble()
        }

    fun getToFive(context: Context): String {
        if (marks.size == 0) return "Для атестации нужно минимум 3 оценки"
        val marks_converted = marks.map{ it -> it.value}.toMutableList()
        var added = 0
        if (marks_converted.average() >= 4.6) return "<font color=#336635>Уже 5</font>"
        while (marks_converted.average() < 4.6) {
            marks_converted += 5
            added++
            if (marks.size >= 50) {
                return ""
            }
        }
        return "До <font color=#336635>5</font> " + context.resources.getQuantityString(R.plurals.five, added).format(added)
    }
    fun getToFive(context: Context, marks: MutableList<Int>): String {
        if (marks.size == 0) return "Для атестации нужно минимум 3 оценки"
        var added = 0
        if (marks.average() >= 4.6) return "<font color=#336635>Уже 5</font>"
        while (marks.average() < 4.6) {
            marks += 5
            added++
            if (marks.size >= 50) {
                return ""
            }
        }
        return "До <font color=#336635>5</font> " + context.resources.getQuantityString(R.plurals.five, added).format(added)
    }

    fun getFourToFour(context: Context): String {
        if (marks.size == 0) return "Для атестации нужно минимум 3 оценки"
        val marks_converted = marks.map{ it -> it.value}.toMutableList()
        var added = 0
        if (marks_converted.average() >= 4.6) return "<font color=#336635>Уже 5</font>"
        if (marks_converted.average() >= 3.6) return "<font color=#e7a700>Уже 4</font>"
        while (marks_converted.average() < 3.6) {
            marks_converted += 4
            added++
            if (marks.size >= 50) {
                return ""
            }
        }
        return "До <font color=#e7a700>4</font> " + context.resources.getQuantityString(R.plurals.four, added).format(added)
    }
    fun getFourToFour(context: Context, marks: MutableList<Int>): String {
        if (marks.size == 0) return "Для атестации нужно минимум 3 оценки"
        var added = 0
        if (marks.average() >= 4.6) return "<font color=#336635>Уже 5</font>"
        if (marks.average() >= 3.6) return "<font color=#e7a700>Уже 4</font>"
        while (marks.average() < 3.6) {
            marks += 4
            added++
            if (marks.size >= 50) {
                return ""
            }
        }
        return "До <font color=#e7a700>4</font> " + context.resources.getQuantityString(R.plurals.four, added).format(added)
    }

    fun getFiveToFour(context: Context): String {
        if (marks.size == 0) return "Для атестации нужно минимум 3 оценки"
        val marks_converted = marks.map{ it -> it.value}.toMutableList()
        var added = 0
        if (marks_converted.average() >= 4.6) return "<font color=#336635>Уже 5</font>"
        if (marks_converted.average() >= 3.6) return "<font color=#e7a700>Уже 4</font>"
        while (marks_converted.average() < 3.6) {
            marks_converted += 5
            added++
            if (marks.size >= 50) {
                return ""
            }
        }
        return "До <font color=#e7a700>4</font> " + context.resources.getQuantityString(R.plurals.five, added).format(added)
    }
    fun getFiveToFour(context: Context, marks: MutableList<Int>): String {
        if (marks.size == 0) return "Для атестации нужно минимум 3 оценки"
        var added = 0
        if (marks.average() >= 4.6) return "<font color=#336635>Уже 5</font>"
        if (marks.average() >= 3.6) return "<font color=#e7a700>Уже 4</font>"
        while (marks.average() < 3.6) {
            marks += 5
            added++
            if (marks.size >= 50) {
                return ""
            }
        }
        return "До <font color=#e7a700>4</font> " + context.resources.getQuantityString(R.plurals.five, added).format(added)
    }
}