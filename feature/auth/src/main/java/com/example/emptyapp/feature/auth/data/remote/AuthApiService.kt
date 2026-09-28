package com.example.emptyapp.feature.auth.data.remote

import com.example.emptyapp.feature.auth.data.remote.dto.LoginRequest
import com.example.emptyapp.feature.auth.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
