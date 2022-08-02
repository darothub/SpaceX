package com.mindera.rocketscience.domain

sealed class UIState {
    data class Success<T>(val data: T) : UIState()
    data class Error(val exception: Throwable) : UIState()
    object Loading : UIState()
    object Nothing : UIState()
}