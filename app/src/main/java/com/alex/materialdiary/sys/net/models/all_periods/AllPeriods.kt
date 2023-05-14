package com.alex.materialdiary.sys.net.models.all_periods

data class AllPeriods (
    var success: Boolean? = null,
    var system: Boolean? = null,
    var message: String? = null,
    var data: MutableList<AllPeriodData>
){}