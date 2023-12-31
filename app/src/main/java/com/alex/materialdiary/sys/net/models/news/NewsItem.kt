package com.alex.materialdiary.sys.net.models.news

import com.google.gson.annotations.SerializedName

data class NewsItem(
                    @SerializedName("SYS_GUID")
                    val sysGuid: String = "",
                    @SerializedName("CATEGORY_FK_KEYS")
                    val categoryFkKeys: List<FkKeyCategory>?,
                    @SerializedName("IMAGE")
                    val image: String = "",
                    @SerializedName("IMAGES")
                    val images: List<ImagesItem>?,
                    @SerializedName("CONTENT")
                    val content: String = "",
                    @SerializedName("NAME")
                    val name: String = "")