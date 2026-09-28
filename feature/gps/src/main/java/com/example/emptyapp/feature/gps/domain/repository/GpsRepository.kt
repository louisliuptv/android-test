package com.example.emptyapp.feature.gps.domain.repository

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.feature.gps.domain.model.Gps

interface GpsRepository {
    suspend fun getGpsList(
        clinicId: Int? = null,
        specialization: String? = null,
    ): ApiResult<List<Gps>>

    suspend fun getGpsDetail(id: Int): ApiResult<Gps>
}
