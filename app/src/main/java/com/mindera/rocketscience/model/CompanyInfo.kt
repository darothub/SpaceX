package com.mindera.rocketscience.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CompanyInfo (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val founder: String,
    val founded: Long,
    val employees: Long,
    val vehicles: Long,
    @SerializedName("launch_sites")
    val launchSites: Long,
    @SerializedName("test_sites")
    val testSites: Long,
    val ceo: String,
    val cto: String,
    val coo: String,
    @SerializedName("cto_propulsion")
    val ctoPropulsion: String,
    val valuation: Long,
    @Embedded
    val headquarters: Headquarters,
    val summary: String
)
data class Headquarters (
    val address: String,
    val city: String,
    val state: String
)