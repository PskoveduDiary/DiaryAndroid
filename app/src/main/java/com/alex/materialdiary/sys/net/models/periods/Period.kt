package com.alex.materialdiary.sys.net.models.periods

import com.alex.materialdiary.sys.net.models.period_marks.Mark
import com.google.gson.annotations.SerializedName

data class Period (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,

    @SerializedName("PERIOD_GUID")
    var periodGuid: String? = null,

    @SerializedName("NAME")
    var name: String? = null,

    @SerializedName("DATE_BEGIN")
    var dateBegin: String? = null,

    @SerializedName("DATE_END")
    var dateEnd: String? = null,

    @SerializedName("GRADE_TYPE_GUID")
    var gradeTypeGuid: Int? = null,

    @SerializedName("MARK")
    var mark: Mark? = null,

    @SerializedName("AVERAGE")
    var average: Double? = null,

    @SerializedName("COUNT")
    var count: Int? = null
)