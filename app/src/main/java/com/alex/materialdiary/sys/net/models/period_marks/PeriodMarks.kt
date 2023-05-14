package com.alex.materialdiary.sys.net.models.period_marks


data class PeriodMarks (
    var success: Boolean? = null,
    var system: Boolean? = null,
    var message: String? = null,
    var data: MutableList<PeriodMarksData>
)