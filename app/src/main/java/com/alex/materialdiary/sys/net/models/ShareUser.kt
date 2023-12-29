package com.alex.materialdiary.sys.net.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareUser(
    val name: String,
    val guid: String,
    val school: String,
    val classname: String,
    val snils: String?,
    val grade: String?
) : Parcelable
