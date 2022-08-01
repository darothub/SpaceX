package com.mindera.rocketscience.core.remote

import com.mindera.rocketscience.model.CompanyInfo
import com.mindera.rocketscience.model.Launches
import retrofit2.http.GET

const val baseUrl = "https://api.spacexdata.com/v3/"
interface ApiService {
    @GET("info")
    suspend fun getCompanyInfo(): CompanyInfo
    @GET("launches")
    suspend fun getLaunches(): Launches
}