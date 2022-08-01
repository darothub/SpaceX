package com.mindera.rocketscience.core.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindera.rocketscience.model.CompanyInfo
import kotlinx.coroutines.flow.Flow
typealias CompanyInfos = List<CompanyInfo>
@Dao
interface CompanyInfoDao: BaseDao<CompanyInfo> {
    @Query("SELECT * FROM companyinfo ")
    fun getCompanyInfo(): Flow<CompanyInfos>
}