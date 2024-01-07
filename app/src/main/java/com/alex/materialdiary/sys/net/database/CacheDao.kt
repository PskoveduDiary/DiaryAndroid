package com.alex.materialdiary.sys.net.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.sql.Timestamp

@Dao
interface CacheDao {
    @Insert(entity = CachedData::class)
    fun insertNewCacheRecord(cache: CachedData)

    @Query("SELECT * FROM cachedData WHERE req_type = :type")
    fun getCachedDataByType(type: String): List<CachedData>
    @Query("SELECT * FROM cachedData WHERE req_type = :type AND optional = :optional")
    fun getCachedDataByTypeAndOptional(type: String, optional: String): List<CachedData>
    @Query("DELETE FROM cachedData WHERE id = :recordId")
    fun deleteCacheRecordById(recordId: Long)
    @Query("DELETE FROM cachedData WHERE actuality < :fromTimestamp AND (req_type == 'marksbyperiod' OR req_type == 'periodmarks')")
    fun flushOldMarksRecords(fromTimestamp: Long)
    @Query("DELETE FROM cachedData WHERE actuality < :fromTimestamp")
    fun flushOldRecords(fromTimestamp: Long)
}