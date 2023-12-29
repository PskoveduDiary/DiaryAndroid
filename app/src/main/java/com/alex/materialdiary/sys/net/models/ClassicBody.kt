package com.alex.materialdiary.sys.net.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClassicBody {
    @SerializedName("apikey")
    @Expose
    var apikey: String? = null

    @SerializedName("guid")
    @Expose
    var guid: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("from")
    @Expose
    var from: String? = null

    @SerializedName("to")
    @Expose
    var to: String? = null

    @SerializedName("pdakey")
    @Expose
    var pdakey: String? = null
}