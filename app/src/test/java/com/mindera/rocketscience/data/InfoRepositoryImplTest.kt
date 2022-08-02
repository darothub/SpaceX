package com.mindera.rocketscience.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindera.rocketscience.core.local.dao.CompanyInfoDao
import com.mindera.rocketscience.core.local.database.SpaceXDatabase
import com.mindera.rocketscience.core.remote.BaseApiService
import com.mindera.rocketscience.domain.info.InfoRepository
import com.mindera.rocketscience.model.CompanyInfo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InfoRepositoryImplTest {
    lateinit var spaceXDatabase: SpaceXDatabase
    lateinit var infoDao: CompanyInfoDao
    lateinit var infoRepository: InfoRepository
    lateinit var apiService: BaseApiService
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        spaceXDatabase = Room.inMemoryDatabaseBuilder(context, SpaceXDatabase::class.java).build()
        infoDao = spaceXDatabase.infoDao
        apiService = ApiTestServiceImpl()
        infoRepository = InfoRepositoryImpl(apiService, infoDao)
    }

    @After
    fun tearDown() {
        spaceXDatabase.close()
    }

    @Test
    fun getCompanyInfo() = runBlocking {
        val info = infoRepository.getCompanyInfo()
        assert(info == apiService.getCompanyInfo())
    }

}