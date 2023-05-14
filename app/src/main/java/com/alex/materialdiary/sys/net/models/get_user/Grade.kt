package com.alex.materialdiary.sys.net.models.get_user

import com.google.gson.annotations.SerializedName

data class Grade (
    @SerializedName("SYS_GUID")
    var sysGuid: String? = null,

    @SerializedName("NAME")
    var name: String? = null,

    @SerializedName("SCHOOL")
    var school: SchoolInfo? = null,

    @SerializedName("GRADE_HEAD")
    var gradeHead: GradeHead? = null
)