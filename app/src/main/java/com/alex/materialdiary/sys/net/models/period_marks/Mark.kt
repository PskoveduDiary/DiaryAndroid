package com.alex.materialdiary.sys.net.models.period_marks

import com.google.gson.annotations.SerializedName

data class Mark (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,

    @SerializedName("DATE")
    var date: String,

    @SerializedName("LONG_NAME")
    var longName: String? = null,

    @SerializedName("SHORT_NAME")
    var shortName: String? = null,

    @SerializedName("VALUE")
    var value: Int
)