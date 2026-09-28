package com.example.emptyapp.feature.auth.data.repository

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.network.api.safeApiCall
import com.example.emptyapp.feature.auth.data.remote.AuthApiService
import com.example.emptyapp.feature.auth.data.remote.dto.LoginRequest
import com.example.emptyapp.feature.auth.data.token.TokenStore
import com.example.emptyapp.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.serialization.SerializationException

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val tokenStore: TokenStore,
) : AuthRepository {

    override suspend fun login(email: String, password: String): ApiResult<Unit> =
        safeApiCall {
            val response = api.login(LoginRequest(email = email, password = password))
            val token = response.token?.takeIf { it.isNotBlank() }
                ?: throw SerializationException("Login response is missing an access token")
            tokenStore.save(token)
        }

    override suspend fun logout() {
        tokenStore.clear()
    }

    override suspend fun isLoggedIn(): Boolean = tokenStore.isSignedIn()
}
