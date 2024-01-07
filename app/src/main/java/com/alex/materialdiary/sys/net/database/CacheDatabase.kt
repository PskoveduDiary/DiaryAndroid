package com.alex.materialdiary.sys.net.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    version = 1,
    entities = [
        CachedData::class
    ]
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}