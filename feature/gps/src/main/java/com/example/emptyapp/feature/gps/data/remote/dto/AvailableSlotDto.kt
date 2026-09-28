package com.example.emptyapp.feature.gps.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AvailableSlotDto(
    val dateTime: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val duration: Int? = null,
    val available: Boolean? = null,
)
