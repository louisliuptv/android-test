package com.example.emptyapp.feature.gps.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Untrusted response model: every field is optional so a missing attribute from
 * the backend never fails deserialization. The mapper supplies safe defaults.
 */
@Serializable
data class GpDto(
    val id: Int? = null,
    val name: String? = null,
    val specialization: String? = null,
    val clinicId: Int? = null,
    val postcode: String? = null,
    val yearsOfExperience: Int? = null,
    val rating: Double? = null,
    val languages: List<String?>? = null,
    val availableSlots: List<AvailableSlotDto?>? = null,
    val clinic: ClinicDto? = null,
)
