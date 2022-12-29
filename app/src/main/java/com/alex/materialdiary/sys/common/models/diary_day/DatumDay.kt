package com.alex.materialdiary.sys.common.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.alex.materialdiary.sys.common.models.diary_day.HomeworkPrevious
import kotlinx.parcelize.Parcelize

@Parcelize
class DatumDay : Parcelable {
    @SerializedName("JOURNAL_SYS_GUID")
    @Expose
    var journalSysGuid: String? = null

    @SerializedName("SUBJECT_SYS_GUID")
    @Expose
    var subjectSysGuid: String? = null

    @SerializedName("SUBJECT_NAME")
    @Expose
    var subjectName: String? = null

    @SerializedName("TEACHER_SYS_GUID")
    @Expose
    var teacherSysGuid: String? = null

    @SerializedName("TEACHER_NAME")
    @Expose
    var teacherName: String? = null

    @SerializedName("CABINET_SYS_GUID")
    @Expose
    var cabinetSysGuid: Any? = null

    @SerializedName("CABINET_NAME")
    @Expose
    var cabinetName: Any? = null

    @SerializedName("LESSON_SYS_GUID")
    @Expose
    var lessonSysGuid: String? = null

    @SerializedName("LESSON_DATE")
    @Expose
    var lessonDate: String? = null

    @SerializedName("LESSON_NUMBER")
    @Expose
    var lessonNumber: Int? = null

    @SerializedName("LESSON_TIME_SYS_GUID")
    @Expose
    var lessonTimeSysGuid: String? = null

    @SerializedName("LESSON_TIME_BEGIN")
    @Expose
    var lessonTimeBegin: String? = null

    @SerializedName("LESSON_TIME_END")
    @Expose
    var lessonTimeEnd: String? = null

    @SerializedName("GRADE_TYPE_SYS_GUID")
    @Expose
    var gradeTypeSysGuid: Any? = null

    @SerializedName("GRADE_TYPE_NAME")
    @Expose
    var gradeTypeName: Any? = null

    @SerializedName("GRADE_TYPE_PERIOD")
    @Expose
    var gradeTypePeriod: Boolean? = null

    @SerializedName("GRADE_TYPE_WEIGHT")
    @Expose
    var gradeTypeWeight: Int? = null

    @SerializedName("GRADE_HEAD_COMMENT")
    @Expose
    var gradeHeadComment: Boolean? = null

    @SerializedName("TOPIC")
    @Expose
    var topic: String? = null

    @SerializedName("HOMEWORK")
    @Expose
    var homework: String? = null

    @SerializedName("HOMEWORK_PREVIOUS")
    @Expose
    var homeworkPrevious: HomeworkPrevious? = null

    @SerializedName("MARKS")
    @Expose
    var marks: List<Mark>? = null

    @SerializedName("ABSENCE")
    @Expose
    var absence: List<Any>? = null

    @SerializedName("NOTES")
    @Expose
    var notes: List<Any>? = null
}