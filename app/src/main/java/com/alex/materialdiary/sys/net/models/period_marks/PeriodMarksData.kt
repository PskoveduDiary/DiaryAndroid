package com.alex.materialdiary.sys.net.models.period_marks

import com.google.gson.annotations.SerializedName
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
) {
    val average: String
        get() {
            if (marks.size == 0) return "Нет оценок"
            var com = 0
            for (i in marks.indices) {
                com += marks[i].value
            }
            return "Средний бал: " + DecimalFormat("#0.00").format((com.toFloat() / marks.size).toDouble())
        }

    fun get_to_five(): String {
        if (marks.size == 0) return "3"
        var sum = 0
        var added = 0
        for (i in marks.indices) {
            sum += marks[i].value
        }
        var average = sum.toFloat() / marks.size
        if (average >= 4.59) return "Уже 5"
        while (average < 4.6) {
            added += 1
            sum += 5
            average = sum.toFloat() / (marks.size + added)
            if (added >= 40) {
                return "Много"
            }
        }
        return added.toString()
    }

    fun get_four_to_four(): String {
        if (marks.size == 0) return "3"
        var sum = 0
        var added = 0
        for (i in marks.indices) {
            sum += marks[i].value
        }
        var average = sum.toFloat() / marks.size
        if (average >= 4.59) return "Уже 5"
        if (average >= 3.59) return "Уже 4"
        while (average < 3.6) {
            added += 1
            sum += 4
            average = sum.toFloat() / (marks.size + added)
            if (added >= 40) {
                return "Много"
            }
        }
        return added.toString()
    }

    fun get_five_to_four(): String {
        if (marks.size == 0) return "3"
        var sum = 0
        var added = 0
        for (i in marks.indices) {
            sum += marks[i].value
        }
        var average = sum.toFloat() / marks.size
        if (average >= 4.59) return "Уже 5"
        if (average >= 3.59) return "Уже 4"
        while (average < 3.6) {
            added += 1
            sum += 5
            average = sum.toFloat() / (marks.size + added)
            if (added >= 40) {
                return "Много"
            }
        }
        return added.toString()
    }
}