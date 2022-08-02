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

    @Query("SELECT * FROM launch WHERE launchYear = :year AND launchSuccess = :result ORDER BY launchDateUnix ASC ")
    fun filterLaunches(year: String, result: Int): Flow<List<Launch>>

    @Query("SELECT * FROM launch " +
            "WHERE launchYear = :year AND launchSuccess = :result " +
            "ORDER BY CASE :order WHEN 0 THEN launchDateUnix END ASC, " +
            "CASE :order WHEN 1 THEN launchDateUnix END DESC")
    fun filterLaunches(year: String, result: Int, order: Int): Flow<List<Launch>>
}