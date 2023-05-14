package com.alex.materialdiary.sys.net.models.kr

data class kr_info(
    val lessonName: String,
    val keyword: MutableList<String> = mutableListOf<String>(),
    var type: TYPES = TYPES.NONE
){
    enum class TYPES{
        FULL,
        MINI,
        MAYBE,
        NONE
    }
}
