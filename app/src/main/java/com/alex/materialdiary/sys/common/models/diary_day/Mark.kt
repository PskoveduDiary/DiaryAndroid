package com.alex.materialdiary.sys.common.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
class Mark : Parcelable {
    @SerializedName("SYS_GUID")
    @Expose
    var sysGuid: String? = null

    @SerializedName("LONG_NAME")
    @Expose
    var longName: String? = null

    @SerializedName("SHORT_NAME")
    @Expose
    var shortName: String? = null

    @SerializedName("VALUE")
    @Expose
    var value: Int? = null
}