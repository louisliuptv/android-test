package com.example.emptyapp.feature.auth.data.repository

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.common.result.map
import com.example.emptyapp.core.network.api.safeApiCall
import com.example.emptyapp.feature.auth.data.remote.AuthApiService
import com.example.emptyapp.feature.auth.data.remote.dto.LoginRequest
import com.example.emptyapp.feature.auth.data.session.SessionManagerImpl
import com.example.emptyapp.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val sessionManager: SessionManagerImpl,
) : AuthRepository {

    override suspend fun login(email: String, password: String): ApiResult<Unit> =
        safeApiCall { api.login(LoginRequest(email = email, password = password)) }
            .map { response -> sessionManager.onAuthenticated(response.token) }

    override suspend fun logout() {
        sessionManager.onLoggedOut()
    }

    override suspend fun isLoggedIn(): Boolean = sessionManager.isAuthenticated()
}
