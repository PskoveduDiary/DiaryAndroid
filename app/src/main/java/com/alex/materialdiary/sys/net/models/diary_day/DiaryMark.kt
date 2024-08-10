package com.alex.materialdiary.sys.net.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryMark (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,

    @SerializedName("LONG_NAME")
    var longName: String? = null,

    @SerializedName("SHORT_NAME")
    var shortName: String? = null,

    @SerializedName("VALUE")
    var value: Int? = null
) : Parcelable