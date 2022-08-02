package com.mindera.rocketscience.data

import android.util.Log
import com.mindera.rocketscience.core.remote.ApiService
import com.mindera.rocketscience.core.remote.BaseApiService
import com.mindera.rocketscience.domain.launches.LaunchRepository
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.Launches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LaunchRepositoryImpl(
    private val apiService: BaseApiService,
    private val launchDataSource: LaunchDataSource
) : LaunchRepository {
    override suspend fun getRemoteLaunches(): List<Launch> {
        val launches = apiService.getLaunches()
        return saveInDB(launches)
    }

    override suspend fun saveInDB(launches: Launches): List<Launch> {
        return launches.map {
            launchDataSource.insert(it)
        }
    }

    override fun getLocalLaunches() = launchDataSource.getLaunches()
    override fun getAllYears(): Flow<List<String>> = launchDataSource.getAllYears()
    override fun filterLaunches(year: String, result: Int): Flow<List<Launch>>
        =  launchDataSource.filterLaunches(year, result)

    override fun filterLaunches(year: String, result: Int, order: Int): Flow<List<Launch>>
         =  launchDataSource.filterLaunches(year, result, order)


}


