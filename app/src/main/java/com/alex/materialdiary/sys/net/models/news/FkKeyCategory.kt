package com.alex.materialdiary.sys.net.models.news

import com.google.gson.annotations.SerializedName

data class FkKeyCategory(@SerializedName("SYS_GUID")
                         val sysGuid: String = "",
                         @SerializedName("DISPLAY")
                         val display: String = "")