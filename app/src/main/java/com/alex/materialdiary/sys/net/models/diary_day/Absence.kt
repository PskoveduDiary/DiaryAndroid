package com.alex.materialdiary.sys.net.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Absence(
    @SerializedName("FULL_NAME")
    val fullName: String,
    @SerializedName("SHORT_NAME")
    val shortName: String,
    @SerializedName("SYS_GUID")
    val sysGuid: String
) : Parcelable