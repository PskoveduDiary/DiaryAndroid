package com.alex.materialdiary.sys.net.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "cachedData")
data class CachedData(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val req_type: String,
    val optional: String? = "",
    val actuality: Long,
    val response: String
)
