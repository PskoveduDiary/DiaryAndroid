package com.alex.materialdiary.sys.net.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeworkPrevious  (
    @SerializedName("DATE")
    var date: String? = null,

    @SerializedName("HOMEWORK")
    var homework: String? = null
) : Parcelable