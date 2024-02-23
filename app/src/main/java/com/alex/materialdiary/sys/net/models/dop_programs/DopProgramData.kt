package com.alex.materialdiary.sys.net.models.dop_programs

data class DopProgramData(
    val address: String,
    val code: String,
    val description: String,
    val eduterm: String?,
    val fileguid: String,
    val goal: String,
    val hours: String?,
    val long_DESCRIPTION: String,
    val mtb: String,
    val name: String,
    val ovz: Int,
    val price: String,
    val publication_DATE: String,
    val results: String,
    val sys_GUID: String
)