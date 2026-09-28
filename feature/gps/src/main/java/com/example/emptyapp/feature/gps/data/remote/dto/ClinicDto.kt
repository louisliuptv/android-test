package com.example.emptyapp.feature.gps.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClinicDto(
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val postcode: String = "",
    val phoneNumber: String = "",
)
