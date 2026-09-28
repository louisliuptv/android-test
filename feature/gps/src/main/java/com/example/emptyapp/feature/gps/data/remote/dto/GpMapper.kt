package com.example.emptyapp.feature.gps.data.remote.dto

import com.example.emptyapp.feature.gps.domain.model.AvailableSlot
import com.example.emptyapp.feature.gps.domain.model.Clinic
import com.example.emptyapp.feature.gps.domain.model.Gps

fun GpDto.toDomain(): Gps = Gps(
    id = id ?: 0,
    name = name.orEmpty(),
    specialization = specialization.orEmpty(),
    clinicId = clinicId ?: 0,
    postcode = postcode.orEmpty(),
    yearsOfExperience = yearsOfExperience ?: 0,
    rating = rating ?: 0.0,
    languages = languages.orEmpty().mapNotNull { it },
    availableSlots = availableSlots.orEmpty().mapNotNull { it?.toDomain() },
    clinic = clinic?.toDomain(),
)

fun AvailableSlotDto.toDomain(): AvailableSlot = AvailableSlot(
    dateTime = dateTime.orEmpty(),
    startTime = startTime.orEmpty(),
    endTime = endTime.orEmpty(),
    duration = duration ?: 0,
    available = available ?: false,
)

fun ClinicDto.toDomain(): Clinic = Clinic(
    id = id ?: 0,
    name = name.orEmpty(),
    address = address.orEmpty(),
    postcode = postcode.orEmpty(),
    phoneNumber = phoneNumber.orEmpty(),
)
