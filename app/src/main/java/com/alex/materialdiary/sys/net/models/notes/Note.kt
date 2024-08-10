package com.alex.materialdiary.sys.net.models.notes

data class Note(
    val text: String,
    val author_uuid: String,
    val public: Boolean,
)
