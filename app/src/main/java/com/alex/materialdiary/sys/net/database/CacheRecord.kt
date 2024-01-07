package com.alex.materialdiary.sys.net.database

data class CacheRecord(
                       val req_type: String,
                       val optional: String? = "",
                       val actuality: Long,
                       val response: String){
    fun toDbEntity(): CachedData = CachedData(
        id = 0,
        req_type = req_type,
        actuality = actuality,
        response = response
    )
}
