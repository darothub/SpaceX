package com.mindera.rocketscience.domain.info

import com.mindera.rocketscience.core.local.dao.CompanyInfos
import com.mindera.rocketscience.model.CompanyInfo
import com.mindera.rocketscience.model.Launches
import kotlinx.coroutines.flow.Flow

interface InfoRepository {
    suspend fun getCompanyInfo() : CompanyInfo
    fun getLocalCompanyInfo() : Flow<CompanyInfos>
}