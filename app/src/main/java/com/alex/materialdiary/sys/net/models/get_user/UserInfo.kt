package com.alex.materialdiary.sys.net.models.get_user


data class UserInfo (
    var success: Boolean = false,
    var system: Boolean? = null,
    var message: String? = null,
    var data: UserData
)