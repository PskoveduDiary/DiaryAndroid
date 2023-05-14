package com.alex.materialdiary.sys.net.models.check_list

data class CheckListShow(
    var name: String,
    var done: Boolean,
    var homework: String?
) {
    fun toLesson(): Lesson {
        return Lesson(name, done)
    }
}
