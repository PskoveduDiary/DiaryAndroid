package com.alex.materialdiary.sys.common.models.diary_day

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
class HomeworkPrevious : Parcelable {
    @SerializedName("DATE")
    @Expose
    var date: String? = null

    @SerializedName("HOMEWORK")
    @Expose
    var homework: String? = null
}