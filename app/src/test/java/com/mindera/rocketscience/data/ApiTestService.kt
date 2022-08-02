package com.mindera.rocketscience.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.mindera.rocketscience.core.remote.BaseApiService
import com.mindera.rocketscience.model.*

interface ApiTestService: BaseApiService {
    override suspend fun getCompanyInfo(): CompanyInfo
    override suspend fun getLaunches(): Launches
}

class ApiTestServiceImpl : ApiTestService {
    override suspend fun getCompanyInfo(): CompanyInfo {
        return CompanyInfo (
            name = "SpaceX",
            founder = "Abdul",
            founded = 2022,
            employees = 20,
            vehicles = 100,
            launchSites = 2000,
            testSites = 3000,
            ceo = "Apple",
            cto = "Android",
            coo = "Cr7",
            ctoPropulsion = "Nigeria Porto",
            valuation = 20000000,
            headquarters = Headquarters(
                address = "Gaia",
                city = "Porto",
                state = "Porto"
            ),
            summary = "All trust God"
        )
    }

    override suspend fun getLaunches(): Launches {
        return emptyList<LaunchDTO>() as Launches
    }

}
