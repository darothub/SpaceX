package com.mindera.rocketscience.core.remote

import com.mindera.rocketscience.model.CompanyInfo
import com.mindera.rocketscience.model.Launches
import retrofit2.http.GET

const val baseUrl = "https://api.spacexdata.com/v3/"
interface ApiService : BaseApiService {
    @GET("info")
    override suspend fun getCompanyInfo(): CompanyInfo
    @GET("launches")
    override suspend fun getLaunches(): Launches
}

interface BaseApiService {
    suspend fun getCompanyInfo(): CompanyInfo
    suspend fun getLaunches(): Launches
}