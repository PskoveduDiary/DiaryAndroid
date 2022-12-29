package com.alex.materialdiary.sys.common.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareUser(
    val name: String,
    val guid: String,
    val school: String,
    val classname: String
) : Parcelable
