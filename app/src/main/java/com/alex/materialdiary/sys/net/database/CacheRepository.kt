package com.alex.materialdiary.sys.net.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Optional

class CacheRepository(private val cacheDao: CacheDao) {
    suspend fun insertNewCacheRecord(cacheRecord: CachedData) {
        withContext(Dispatchers.IO) {
            cacheDao.insertNewCacheRecord(cacheRecord)
        }
    }

    suspend fun getCacheByType(type: String): List<CachedData> {
        return withContext(Dispatchers.IO) {
            return@withContext cacheDao.getCachedDataByType(type)
        }
    }
    suspend fun getCacheByTypeAndOptional(type: String, optional: String): List<CachedData> {
        return withContext(Dispatchers.IO) {
            return@withContext cacheDao.getCachedDataByTypeAndOptional(type, optional)
        }
    }

    suspend fun removeCacheRecordById(id: Long) {
        withContext(Dispatchers.IO) {
            cacheDao.deleteCacheRecordById(id)
        }
    }
}