package com.alex.materialdiary.sys.net.models.all_periods

import com.google.gson.annotations.SerializedName

data class AllPeriodData (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,
    @SerializedName("PERIOD_GUID")
    var periodGuid: String? = null,
    @SerializedName("NAME")
    var name: String? = null,
    @SerializedName("DATE_BEGIN")
    var dateBegin: String,
    @SerializedName("DATE_END")
    var dateEnd: String,
    @SerializedName("GRADE_TYPE_GUID")
    var gradeTypeGuid: String? = null
)