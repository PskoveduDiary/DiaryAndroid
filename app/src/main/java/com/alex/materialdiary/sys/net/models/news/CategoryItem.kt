package com.alex.materialdiary.sys.net.models.news

import com.google.gson.annotations.SerializedName

data class CategoryItem(@SerializedName("SYS_GUID")
                        val sysGuid: String = "",
                        @SerializedName("name")
                        val name: String = "")