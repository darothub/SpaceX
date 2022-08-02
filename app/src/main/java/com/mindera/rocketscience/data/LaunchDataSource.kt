package com.mindera.rocketscience.data

import android.util.Log
import com.mindera.rocketscience.core.local.dao.LaunchDao
import com.mindera.rocketscience.core.remote.ApiService
import com.mindera.rocketscience.domain.Mapper
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.LaunchDTO
import com.mindera.rocketscience.model.Launches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Year

class LaunchDataSource(
    private val dao: LaunchDao,
    private val mapper: Mapper<LaunchDTO, Launch>
) {
    suspend fun insert(launchDto: LaunchDTO): Launch {
        val launch = mapper.toModel(launchDto)
        dao.insert(launch)
        return launch
    }
    fun getLaunches() = dao.getLaunches()

    fun getAllYears() = dao.getGetAllYears()

    fun filterLaunches(year: String, result: Int) = dao.filterLaunches(year, result)
    fun filterLaunches(year: String, result: Int, order: Int) = dao.filterLaunches(year, result, order)
}

