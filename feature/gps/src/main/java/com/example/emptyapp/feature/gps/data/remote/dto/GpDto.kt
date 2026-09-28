package com.example.emptyapp.feature.gps.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GpDto(
    val id: Int,
    val name: String,
    val specialization: String = "",
    val clinicId: Int = 0,
    val postcode: String = "",
    val yearsOfExperience: Int = 0,
    val rating: Double = 0.0,
    val languages: List<String> = emptyList(),
    val availableSlots: List<AvailableSlotDto> = emptyList(),
    val clinic: ClinicDto? = null,
)
