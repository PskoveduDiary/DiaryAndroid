package com.alex.materialdiary.sys.net.models.get_user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData (
    @SerializedName("LOGIN")
    var login: String? = null,

    @SerializedName("SURNAME")
    var surname: String? = null,

    @SerializedName("NAME")
    var name: String? = null,

    @SerializedName("SECONDNAME")
    var secondname: String? = null,

    @SerializedName("EMAIL")
    var email: String? = null,

    @SerializedName("CONFIRMATION")
    var confirmation: String? = null,

    @SerializedName("CONFIRM_EXPIRATION")
    var confirmExpiration: Int? = null,

    @SerializedName("SESSION_ID")
    var sessionId: String? = null,

    @SerializedName("SCHOOLS")
    var schools: List<Schools>
) : Parcelable