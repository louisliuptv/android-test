package com.example.emptyapp.feature.gps.domain.model

data class Gps(
    val id: Int,
    val name: String,
    val specialization: String,
    val clinicId: Int,
    val postcode: String,
    val yearsOfExperience: Int,
    val rating: Double,
    val languages: List<String>,
    val availableSlots: List<AvailableSlot> = emptyList(),
    val clinic: Clinic? = null,
)
