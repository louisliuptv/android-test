package com.example.emptyapp.feature.gps.domain.usecase

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.feature.gps.domain.model.Gps
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import javax.inject.Inject

class GetGpsListUseCase @Inject constructor(
    private val repository: GpsRepository,
) {
    suspend operator fun invoke(
        clinicId: Int? = null,
        specialization: String? = null,
    ): ApiResult<List<Gps>> = repository.getGpsList(clinicId, specialization)
}
