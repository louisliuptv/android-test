package com.example.emptyapp.feature.gps.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClinicDto(
    val id: Int? = null,
    val name: String? = null,
    val address: String? = null,
    val postcode: String? = null,
    val phoneNumber: String? = null,
)
