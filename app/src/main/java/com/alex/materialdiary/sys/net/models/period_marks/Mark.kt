package com.alex.materialdiary.sys.net.models.period_marks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    var value: Int,
    @SerializedName("GRADE_TYPE_NAME")
    var typeName: String? = "Тип не указан"
) : Parcelable