package com.mindera.rocketscience.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mindera.rocketscience.domain.info.InfoRepository
import com.mindera.rocketscience.domain.launches.LaunchRepository

class MainViewModelFactory(
    private val infoRepository: InfoRepository,
    private val launchRepository: LaunchRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(infoRepository, launchRepository) as T
    }
}