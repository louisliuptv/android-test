package com.example.emptyapp.feature.gps.data.repository

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.common.result.map
import com.example.emptyapp.core.network.api.safeApiCall
import com.example.emptyapp.feature.gps.data.remote.GpsApiService
import com.example.emptyapp.feature.gps.data.remote.dto.toDomain
import com.example.emptyapp.feature.gps.domain.model.Gps
import com.example.emptyapp.feature.gps.domain.repository.GpsRepository
import javax.inject.Inject

class GpsRepositoryImpl @Inject constructor(
    private val api: GpsApiService,
) : GpsRepository {

    override suspend fun getGpsList(clinicId: Int?, specialization: String?): ApiResult<List<Gps>> =
        safeApiCall { api.getGps(clinicId = clinicId, specialization = specialization) }
            .map { dtos -> dtos.map { it.toDomain() } }

    override suspend fun getGpsDetail(id: Int): ApiResult<Gps> =
        safeApiCall { api.getGp(id) }.map { it.toDomain() }
}
