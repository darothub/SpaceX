package com.mindera.rocketscience.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.rocketscience.domain.UIState
import com.mindera.rocketscience.domain.info.InfoRepository
import com.mindera.rocketscience.domain.launches.LaunchRepository
import com.mindera.rocketscience.domain.toInt
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
    private val _infoFlow = MutableStateFlow<CompanyInfo?>(null)
    val infoFlow = _infoFlow.asStateFlow()

    private val _launchesFlowState = MutableStateFlow<UIState>(UIState.Nothing)
    val launchesFlowState = _launchesFlowState.asStateFlow()

    fun getCompanyInfoAndLaunches() {
        _launchesFlowState.value = UIState.Loading
        val launchListFlow = launchRepository.getLocalLaunches()
        val infoListFlow = infoRepository.getLocalCompanyInfo()
        infoListFlow.combine(launchListFlow){ infolist, launchlist  ->
            if (launchlist.isEmpty()){
                _launchesFlowState.value = UIState.Success(getRemoteLaunches())
            }
            else{
                _launchesFlowState.value = UIState.Success(launchlist)
            }
            if (infolist.isEmpty()){
                _infoFlow.value = getRemoteCompanyInfo()
            }
           else{
                infolist.map {
                    _infoFlow.value = it
                }
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
    fun filterLaunches(year: String, result: Int, order: Int) {
        _launchesFlowState.value = UIState.Loading
        launchRepository.filterLaunches(year, result, order).map { res ->
            _launchesFlowState.value = UIState.Success(res)
        }.launchIn(viewModelScope)
    }
}