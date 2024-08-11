package com.alex.materialdiary.sys.net.models.get_user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participant (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,

    @SerializedName("SURNAME")
    var surname: String? = null,

    @SerializedName("NAME")
    var name: String? = null,

    @SerializedName("SECONDNAME")
    var secondname: String? = null,

    @SerializedName("SEX")
    var sex: String? = null,

    @SerializedName("GRADE")
    var grade: Grade? = null
) : Parcelable