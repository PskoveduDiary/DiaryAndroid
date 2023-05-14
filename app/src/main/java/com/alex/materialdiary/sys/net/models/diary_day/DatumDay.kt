package com.alex.materialdiary.sys.net.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DatumDay (
    @SerializedName("JOURNAL_SYS_GUID")
    var journalSysGuid: String? = null,

    @SerializedName("SUBJECT_SYS_GUID")
    var subjectSysGuid: String? = null,

    @SerializedName("SUBJECT_NAME")
    var subjectName: String? = null,

    @SerializedName("TEACHER_SYS_GUID")
    var teacherSysGuid: String? = null,

    @SerializedName("TEACHER_NAME")
    var teacherName: String? = null,

    @SerializedName("CABINET_SYS_GUID")
    var cabinetSysGuid: String? = null,

    @SerializedName("CABINET_NAME")
    var cabinetName: String? = null,

    @SerializedName("LESSON_SYS_GUID")
    var lessonSysGuid: String? = null,

    @SerializedName("LESSON_DATE")
    var lessonDate: String? = null,

    @SerializedName("LESSON_NUMBER")
    var lessonNumber: Int? = null,

    @SerializedName("LESSON_TIME_SYS_GUID")
    var lessonTimeSysGuid: String? = null,

    @SerializedName("LESSON_TIME_BEGIN")
    var lessonTimeBegin: String? = null,

    @SerializedName("LESSON_TIME_END")
    var lessonTimeEnd: String? = null,

    @SerializedName("GRADE_TYPE_SYS_GUID")
    var gradeTypeSysGuid: String? = null,

    @SerializedName("GRADE_TYPE_NAME")
    var gradeTypeName: String? = null,

    @SerializedName("GRADE_TYPE_PERIOD")
    var gradeTypePeriod: Boolean? = null,

    @SerializedName("GRADE_TYPE_WEIGHT")
    var gradeTypeWeight: Int? = null,

    @SerializedName("GRADE_HEAD_COMMENT")
    var gradeHeadComment: Boolean? = null,

    @SerializedName("TOPIC")
    var topic: String? = null,

    @SerializedName("HOMEWORK")
    var homework: String? = null,

    @SerializedName("HOMEWORK_PREVIOUS")
    var homeworkPrevious: HomeworkPrevious? = null,

    @SerializedName("MARKS")
    var marks: List<Mark>? = null,

    @SerializedName("ABSENCE")
    var absence: List<String>? = null,

    @SerializedName("NOTES")
    var notes: List<String>? = null,
)  : Parcelable