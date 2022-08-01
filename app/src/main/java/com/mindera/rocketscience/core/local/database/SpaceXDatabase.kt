package com.mindera.rocketscience.core.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mindera.rocketscience.core.local.dao.BaseDao
import com.mindera.rocketscience.core.local.dao.CompanyInfoDao
import com.mindera.rocketscience.core.local.dao.LaunchDao
//import com.mindera.rocketscience.data.local.dao.BaseDao
import com.mindera.rocketscience.model.CompanyInfo
import com.mindera.rocketscience.model.Launch

@Database(
    entities = [CompanyInfo::class, Launch::class],
    version = 1,
    exportSchema = false
)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract val infoDao: CompanyInfoDao
    abstract val launchDao: LaunchDao
    companion object {
        @Volatile
        private var instance: SpaceXDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            SpaceXDatabase::class.java, "spacexdb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}