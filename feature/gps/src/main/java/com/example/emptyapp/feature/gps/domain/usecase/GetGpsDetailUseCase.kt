package com.example.emptyapp.feature.gps.domain.usecase

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.feature.gps.domain.model.Gps
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import javax.inject.Inject

class GetGpsDetailUseCase @Inject constructor(
    private val repository: GpsRepository,
) {
    suspend operator fun invoke(id: Int): ApiResult<Gps> = repository.getGpsDetail(id)
}
