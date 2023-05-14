package com.alex.materialdiary.sys.net.models.check_list

data class CheckList(
    var date: String,
    var uuid: String,
    var lessons: MutableList<Lesson>
)
