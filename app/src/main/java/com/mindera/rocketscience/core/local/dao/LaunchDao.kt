package com.mindera.rocketscience.core.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.mindera.rocketscience.model.Launch
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao: BaseDao<Launch> {
    @Query("SELECT * FROM launch ")
    fun getLaunches(): Flow<List<Launch>>

    @Query("SELECT DISTINCT launchYear FROM LAUNCH")
    fun getGetAllYears(): Flow<List<String>>

    @Query("SELECT * FROM LAUNCH WHERE launchYear = :year AND launchSuccess = :result ORDER BY launchYear ASC ")
    fun filterLaunches(year: String, result: Int): Flow<List<Launch>>
}