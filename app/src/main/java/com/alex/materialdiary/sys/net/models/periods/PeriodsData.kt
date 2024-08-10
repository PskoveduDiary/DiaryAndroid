package com.alex.materialdiary.sys.net.models.periods

import com.google.gson.annotations.SerializedName

class PeriodsData {
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null

    @SerializedName("NAME")
    var name: String? = null

    @SerializedName("PERIODS")
    var periods: List<Period>? = null
}