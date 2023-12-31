package com.alex.materialdiary.sys.net.models.news

import com.google.gson.annotations.SerializedName

data class News(@SerializedName("news")
                val news: List<NewsItem>?,
                @SerializedName("category")
                val category: List<CategoryItem>?)