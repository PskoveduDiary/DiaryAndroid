package com.alex.materialdiary.sys.net.models.diary_day

data class DiaryDay (
    var success: Boolean? = null,
    var system: Boolean? = null,
    var message: String? = null,
    var data: MutableList<DiaryDayData>
)