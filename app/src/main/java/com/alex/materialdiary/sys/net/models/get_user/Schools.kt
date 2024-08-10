package com.alex.materialdiary.sys.net.models.get_user

import com.google.gson.annotations.SerializedName

data class Schools (
    @SerializedName("ROLES")
    var roles: List<String>,

    @SerializedName("SCHOOL")
    var school: SchoolInfo? = null,

    @SerializedName("GOVERNMENT")
    var government: Any? = null,

    @SerializedName("TEACHER")
    var teacher: Teacher? = null,

    @SerializedName("PARENT")
    var parent: Parent? = null,

    @SerializedName("PARTICIPANT")
    var participant: Participant? = null,

    @SerializedName("USER_GRADES")
    var userGrades: List<Any>? = null,

    @SerializedName("USER_PARTICIPANTS")
    var userParticipants: List<Participant>? = null,

    @SerializedName("USER_PARENTS")
    var userParents: List<Parent>? = null
)