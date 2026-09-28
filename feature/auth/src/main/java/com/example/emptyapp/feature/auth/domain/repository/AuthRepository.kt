package com.example.emptyapp.feature.auth.domain.repository

import com.example.emptyapp.core.common.result.ApiResult

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<Unit>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}
