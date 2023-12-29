package com.alex.materialdiary.sys.net.models.marks_average

data class MarksAverage(
    val actuality: String,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean,
    val system: Boolean
)