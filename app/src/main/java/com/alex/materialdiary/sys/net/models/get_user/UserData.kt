package com.alex.materialdiary.sys.net.models.get_user

import com.google.gson.annotations.SerializedName

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
    var confirmExpiration: Any? = null,

    @SerializedName("SESSION_ID")
    var sessionId: Any? = null,

    @SerializedName("SCHOOLS")
    var schools: List<Schools>
)