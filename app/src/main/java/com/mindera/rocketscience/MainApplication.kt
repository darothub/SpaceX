package com.mindera.rocketscience

import android.app.Application
import com.google.gson.GsonBuilder
import com.mindera.rocketscience.core.local.dao.CompanyInfoDao
import com.mindera.rocketscience.core.local.dao.LaunchDao
import com.mindera.rocketscience.core.local.database.SpaceXDatabase
import com.mindera.rocketscience.core.remote.ApiService
import com.mindera.rocketscience.core.remote.baseUrl
import com.mindera.rocketscience.data.InfoRepositoryImpl
import com.mindera.rocketscience.data.LaunchDataSource
import com.mindera.rocketscience.data.LaunchMapper
import com.mindera.rocketscience.data.LaunchRepositoryImpl
import com.mindera.rocketscience.domain.launches.LaunchRepository
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        infoDao = SpaceXDatabase(this).infoDao
        launchDao = SpaceXDatabase(this).launchDao
    }
    companion object {
        lateinit var infoDao: CompanyInfoDao
        lateinit var launchDao: LaunchDao
        private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
        private fun provideOkHttpClient(): okhttp3.OkHttpClient {
            return okhttp3.OkHttpClient.Builder()
                .addInterceptor(provideLoggingInterceptor())
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .build()
        }
        fun getInfoRepository() = InfoRepositoryImpl(getRetrofit(), infoDao)
        private fun getLaunchDataSource() = LaunchDataSource(launchDao, LaunchMapper())
        fun getLaunchRepository() = LaunchRepositoryImpl(getRetrofit(), getLaunchDataSource())

        private fun getRetrofit(): ApiService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(provideOkHttpClient())
                .build()
                .create(ApiService::class.java)
        }
    }
}