package com.example.emptyapp.feature.gps.data.remote.dto

import com.example.emptyapp.feature.gps.domain.model.AvailableSlot
import com.example.emptyapp.feature.gps.domain.model.Clinic
import com.example.emptyapp.feature.gps.domain.model.Gps
import org.junit.Assert.assertEquals
import org.junit.Test

class GpMapperTest {

    @Test
    fun `maps every field from dto to domain`() {
        val dto = GpDto(
            id = 7,
            name = "Dr Who",
            specialization = "General Practitioner",
            clinicId = 3,
            postcode = "3000",
            yearsOfExperience = 9,
            rating = 4.2,
            languages = listOf("English", "Mandarin"),
            availableSlots = listOf(
                AvailableSlotDto(
                    dateTime = "2026-09-28T01:38:06.864Z",
                    startTime = "09:00",
                    endTime = "09:30",
                    duration = 30,
                    available = true,
                ),
            ),
            clinic = ClinicDto(
                id = 3,
                name = "City Clinic",
                address = "1 Main St",
                postcode = "3000",
                phoneNumber = "+123456",
            ),
        )

        val gps = dto.toDomain()

        assertEquals(
            Gps(
                id = 7,
                name = "Dr Who",
                specialization = "General Practitioner",
                clinicId = 3,
                postcode = "3000",
                yearsOfExperience = 9,
                rating = 4.2,
                languages = listOf("English", "Mandarin"),
                availableSlots = listOf(
                    AvailableSlot(
                        dateTime = "2026-09-28T01:38:06.864Z",
                        startTime = "09:00",
                        endTime = "09:30",
                        duration = 30,
                        available = true,
                    ),
                ),
                clinic = Clinic(
                    id = 3,
                    name = "City Clinic",
                    address = "1 Main St",
                    postcode = "3000",
                    phoneNumber = "+123456",
                ),
            ),
            gps,
        )
    }

    @Test
    fun `uses dto defaults when optional fields are absent`() {
        val dto = GpDto(id = 1, name = "Dr Minimal")

        val gps = dto.toDomain()

        assertEquals("", gps.specialization)
        assertEquals("", gps.postcode)
        assertEquals(0, gps.yearsOfExperience)
        assertEquals(0.0, gps.rating, 0.0)
        assertEquals(emptyList<String>(), gps.languages)
        assertEquals(emptyList<AvailableSlot>(), gps.availableSlots)
        assertEquals(null, gps.clinic)
    }
}
