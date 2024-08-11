package com.alex.materialdiary.sys.net.models.get_user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schools (
    @SerializedName("ROLES")
    var roles: List<String>,

    @SerializedName("SCHOOL")
    var school: SchoolInfo? = null,

//    @SerializedName("GOVERNMENT")
//    var government: Any? = null,

    @SerializedName("TEACHER")
    var teacher: Teacher? = null,

    @SerializedName("PARENT")
    var parent: Parent? = null,

    @SerializedName("PARTICIPANT")
    var participant: Participant? = null,

    @SerializedName("USER_GRADES")
    var userGrades: List<Grade>? = null,

    @SerializedName("USER_PARTICIPANTS")
    var userParticipants: List<Participant>? = null,

    @SerializedName("USER_PARENTS")
    var userParents: List<Parent>? = null
) : Parcelable