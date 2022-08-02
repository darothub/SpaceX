package com.mindera.rocketscience.domain.launches

import com.mindera.rocketscience.model.CompanyInfo
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.Launches
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {
    suspend fun getRemoteLaunches() : List<Launch>
    suspend fun saveInDB(launches: Launches): List<Launch>
    fun getLocalLaunches(): Flow<List<Launch>>
    fun getAllYears(): Flow<List<String>>
    fun filterLaunches(year: String, result: Int): Flow<List<Launch>>
    fun filterLaunches(year: String, result: Int, order:Int): Flow<List<Launch>>
}
