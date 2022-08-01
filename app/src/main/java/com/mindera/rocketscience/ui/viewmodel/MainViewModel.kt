package com.mindera.rocketscience.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.rocketscience.domain.info.InfoRepository
import com.mindera.rocketscience.domain.launches.LaunchRepository
import com.mindera.rocketscience.model.CompanyInfo
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.Order
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MainViewModel(
    private val infoRepository: InfoRepository,
    private val launchRepository: LaunchRepository
): ViewModel() {
    private val _launchesFlow = MutableStateFlow<List<Launch>>(emptyList())
    val launchesFlow = _launchesFlow.asStateFlow()
    private val _infoFlow = MutableStateFlow<List<CompanyInfo>>(emptyList())
    val infoFlow = _infoFlow.asStateFlow()


    fun getLaunches() {
        val launchListFlow = launchRepository.getLocalLaunches()
        launchListFlow.map{ launches->
            if (launches.isEmpty()){
                _launchesFlow.value = getRemoteLaunches()
            }
            else{
                _launchesFlow.value = launches
            }

        }.launchIn(viewModelScope)
    }

    suspend fun getCompanyInfo() = suspendCancellableCoroutine<CompanyInfo>{ cont ->
        val infoListFlow = infoRepository.getLocalCompanyInfo()
        infoListFlow.map { infos ->
            if(infos.isEmpty()){
                cont.resume(getRemoteCompanyInfo())
                return@map
            }
            else{
                cont.resume(infos[0])
                return@map
            }
        }.launchIn(viewModelScope)
    }
    private suspend fun getRemoteCompanyInfo() = infoRepository.getCompanyInfo()

    private suspend fun getRemoteLaunches() = launchRepository.getRemoteLaunches()

    fun getAllYears() = launchRepository.getAllYears().shareIn(
        viewModelScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )

    fun filterLaunches(year: String, result: Int, order: Order) {
        Log.d("FilterVM", "Year $year, result: $result, order:$order")
        launchRepository.filterLaunches(year, result).map {

            return@map when(order){
                is Order.DESC -> it.reversed()
                else -> it
            }
        }.map { res ->
             _launchesFlow.value = res
         }.launchIn(viewModelScope)
    }
}