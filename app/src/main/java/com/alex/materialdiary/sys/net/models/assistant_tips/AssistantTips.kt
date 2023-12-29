package com.alex.materialdiary.sys.net.models.assistant_tips

import android.provider.ContactsContract

data class AssistantTips(
    val actuality: String,
    val `data`: List<TipData>,
    val message: String,
    val success: Boolean,
    val system: Boolean
)