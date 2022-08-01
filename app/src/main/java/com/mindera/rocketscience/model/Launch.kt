package com.mindera.rocketscience.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

typealias Launches = ArrayList<LaunchDTO>
@Entity
data class Launch(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val missionName: String,
    val launchYear: String,
    val launchDate: String,
    val launchDateUnix: Long,
    val launchSuccess: Boolean?=null,
    @Embedded
    val rocket: Rocket?=null,
    @Embedded
    val links: Links?=null,
)
data class LaunchDTO (
    @SerializedName("flight_number")
    val flightNumber: Long,
    @SerializedName("mission_name")
    val missionName: String,
    @SerializedName("mission_id")
    val missionID: List<String>,
    val upcoming: Boolean,
    @SerializedName("launch_year")
    val launchYear: String,
    @SerializedName("launch_date_unix")
    val launchDateUnix: Long,
    val launchDateUTC: String,
    @SerializedName("launch_date_local")
    val launchDateLocal: String,
    val isTentative: Boolean,
    val tentativeMaxPrecision: String,
    val tbd: Boolean,
    val launchWindow: Long? = null,
    val rocket: Rocket,
    val ships: List<String>,
    val telemetry: Telemetry,
    val launchSite: LaunchSite,
    @SerializedName("launch_success")
    val launchSuccess: Boolean,
    val launchFailureDetails: LaunchFailureDetails? = null,
    val links: Links,
    val details: String? = null,
    val staticFireDateUTC: String? = null,
    val staticFireDateUnix: Long? = null,
    val timeline: Map<String, Long?>? = null,
    val crew: List<Any?>? = null,
    val lastDateUpdate: String? = null,
    val lastLlLaunchDate: String? = null,
    val lastLlUpdate: String? = null,
    val lastWikiLaunchDate: String? = null,
    val lastWikiRevision: String? = null,
    val lastWikiUpdate: String? = null,
    val launchDateSource: String? = null
)

data class LaunchFailureDetails (
    val time: Long,
    val altitude: Long? = null,
    val reason: String
)

data class LaunchSite (
    val siteID: String,
    val siteName: String,
    val siteNameLong: String
)

data class Links (
    @SerializedName("mission_patch")
    val missionPatch: String? = null,
    @SerializedName("mission_patch_small")
    val missionPatchSmall: String? = null,
    @SerializedName("reddit_campaign")
    val redditCampaign: String? = null,
    @SerializedName("reddit_launch")
    val redditLaunch: String? = null,
    @SerializedName("reddit_recovery")
    val redditRecovery: String? = null,
    @SerializedName("reddit_media")
    val redditMedia: String? = null,
    val presskit: String? = null,
    @SerializedName("article_link")
    val articleLink: String? = null,
    val wikipedia: String? = null,
    @SerializedName("video_link")
    val videoLink: String? = null,
    @SerializedName("youtube_iD")
    val youtubeID: String? = null,
)

data class Rocket (
    @SerializedName("rocket_id")
    val rocketID: String,
    @SerializedName("rocket_name")
    val rocketName: String,
    @SerializedName("rocket_type")
    val rocketType: String,
//    val firstStage: FirstStage,
//    val secondStage: SecondStage,
//    val fairings: Fairings? = null
)

data class Fairings (
    val reused: Boolean? = null,
    val recoveryAttempt: Boolean? = null,
    val recovered: Boolean? = null,
    val ship: String? = null
)
data class FirstStage (
    val cores: List<Core>
)

data class Core (
    val coreSerial: String? = null,
    val flight: Long? = null,
    val block: Long? = null,
    val gridfins: Boolean? = null,
    val legs: Boolean? = null,
    val reused: Boolean? = null,
    val landSuccess: Boolean? = null,
    val landingIntent: Boolean? = null,
    val landingType: String? = null,
    val landingVehicle: String? = null
)

data class SecondStage (
    val block: Long? = null,
    val payloads: List<Payload>
)

data class Payload (
    val payloadID: String,
    val noradID: List<Long>,
    val reused: Boolean,
    val customers: List<String>,
    val nationality: String? = null,
    val manufacturer: String? = null,
    val payloadType: String,
    val payloadMassKg: Double? = null,
    val payloadMassLbs: Double? = null,
    val orbit: String,
    val orbitParams: OrbitParams,
    val capSerial: String? = null,
    val massReturnedKg: Double? = null,
    val massReturnedLbs: Double? = null,
    val flightTimeSEC: Long? = null,
    val cargoManifest: String? = null,
    val uid: String? = null
)

data class OrbitParams (
    val referenceSystem: String? = null,
    val regime: String? = null,
    val longitude: Double? = null,
    val semiMajorAxisKM: Double? = null,
    val eccentricity: Double? = null,
    val periapsisKM: Double? = null,
    val apoapsisKM: Double? = null,
    val inclinationDeg: Double? = null,
    val periodMin: Double? = null,
    val lifespanYears: Double? = null,
    val epoch: String? = null,
    val meanMotion: Double? = null,
    val raan: Double? = null,
    val argOfPericenter: Double? = null,
    val meanAnomaly: Double? = null
)

data class Telemetry (
    val flightClub: String? = null
)

