package com.mindera.rocketscience.data

import com.mindera.rocketscience.domain.Mapper
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.LaunchDTO

class LaunchMapper: Mapper<LaunchDTO, Launch> {
    override fun toModel(value: LaunchDTO): Launch {
        return Launch(
            missionName = value.missionName,
            launchYear = value.launchYear,
            launchDate = value.launchDateLocal,
            launchSuccess = value.launchSuccess,
            rocket = value.rocket,
            links = value.links,
            launchDateUnix = value.launchDateUnix
        )
    }
}