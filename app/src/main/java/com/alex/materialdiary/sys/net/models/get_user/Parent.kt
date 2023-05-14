package com.alex.materialdiary.sys.net.models.get_user

import com.google.gson.annotations.SerializedName

data class Parent (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,

    @SerializedName("SURNAME")
    var surname: String? = null,

    @SerializedName("NAME")
    var name: String? = null,

    @SerializedName("SECONDNAME")
    var secondname: String? = null,

    @SerializedName("SEX")
    var sex: Any? = null,

    @SerializedName("SCHOOL")
    var school: SchoolInfo? = null
)