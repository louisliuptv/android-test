package com.example.emptyapp.feature.gps.domain.model

data class AvailableSlot(
    val dateTime: String,
    val startTime: String,
    val endTime: String,
    val duration: Int,
    val available: Boolean,
)
