package com.alex.materialdiary.sys.common.models.marks

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val marks: MutableList<Mark>,
    val name: String
) : Parcelable
