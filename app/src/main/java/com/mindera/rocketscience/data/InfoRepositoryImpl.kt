package com.mindera.rocketscience.data

import com.mindera.rocketscience.core.local.dao.CompanyInfoDao
import com.mindera.rocketscience.core.remote.ApiService
import com.mindera.rocketscience.core.remote.BaseApiService
import com.mindera.rocketscience.domain.info.InfoRepository
import com.mindera.rocketscience.model.CompanyInfo


class InfoRepositoryImpl(
    private val apiService: BaseApiService,
    private val companyInfoDao: CompanyInfoDao
) : InfoRepository {
    override suspend fun getCompanyInfo(): CompanyInfo {
        val info = apiService.getCompanyInfo()
        return saveInDB(info)
    }

    private suspend fun saveInDB(info: CompanyInfo): CompanyInfo {
        companyInfoDao.insert(info)
        return info
    }

    override fun getLocalCompanyInfo() = companyInfoDao.getCompanyInfo()
}