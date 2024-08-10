package com.alex.materialdiary.sys.net.models.notes

data class NoteRequest(
    val text: String,
    val public: Boolean,
    val lesson_uuid: String? = null,
)
