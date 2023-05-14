package com.alex.materialdiary.sys.net.models.periods

data class Periods (
    var success: Boolean? = null,
    var system: Boolean? = null,
    var message: String? = null,
    var data: List<Datum>
)