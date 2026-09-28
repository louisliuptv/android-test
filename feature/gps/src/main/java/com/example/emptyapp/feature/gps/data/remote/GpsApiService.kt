package com.example.emptyapp.feature.gps.data.remote

import com.example.emptyapp.feature.gps.data.remote.dto.GpDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GpsApiService {

    @GET("gps")
    suspend fun getGps(
        @Query("clinicId") clinicId: Int? = null,
        @Query("specialization") specialization: String? = null,
    ): List<GpDto>

    @GET("gps/{id}")
    suspend fun getGp(@Path("id") id: Int): GpDto
}
