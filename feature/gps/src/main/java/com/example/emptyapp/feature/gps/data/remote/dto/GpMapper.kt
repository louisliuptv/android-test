package com.example.emptyapp.feature.gps.data.remote.dto

import com.example.emptyapp.feature.gps.domain.model.AvailableSlot
import com.example.emptyapp.feature.gps.domain.model.Clinic
import com.example.emptyapp.feature.gps.domain.model.Gps

fun GpDto.toDomain(): Gps = Gps(
    id = id,
    name = name,
    specialization = specialization,
    clinicId = clinicId,
    postcode = postcode,
    yearsOfExperience = yearsOfExperience,
    rating = rating,
    languages = languages,
    availableSlots = availableSlots.map { it.toDomain() },
    clinic = clinic?.toDomain(),
)

fun AvailableSlotDto.toDomain(): AvailableSlot = AvailableSlot(
    dateTime = dateTime,
    startTime = startTime,
    endTime = endTime,
    duration = duration,
    available = available,
)

fun ClinicDto.toDomain(): Clinic = Clinic(
    id = id,
    name = name,
    address = address,
    postcode = postcode,
    phoneNumber = phoneNumber,
)
