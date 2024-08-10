package com.alex.materialdiary.sys.net.models.marks_average

data class MarksAverageData(
    val DATE_BEGIN: String,
    val DATE_END: String,
    val GRADE_TYPE_GUID: String,
    val MARKS_AVERAGE: Double,
    val MARKS_AVERAGE_WEIGHTED: Double,
    val MARKS_TOTAL: Int,
    val NAME: String,
    val PERIOD_GUID: String,
    val SYS_GUID: String
)