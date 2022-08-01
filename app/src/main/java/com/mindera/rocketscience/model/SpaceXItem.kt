package com.mindera.rocketscience.model

sealed class SpaceXItem {
    abstract val id: Long
    data class Company(val info: CompanyInfo): SpaceXItem() {
        override val id: Long
            get() = info.id
    }
    data class Launches(val launch: Launch ): SpaceXItem() {
        override val id: Long
            get() = launch.id
    }
    object Header: SpaceXItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }
}