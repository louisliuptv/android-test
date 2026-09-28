package com.example.emptyapp.feature.gps.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AvailableSlotDto(
    val dateTime: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val duration: Int = 0,
    val available: Boolean = false,
)
