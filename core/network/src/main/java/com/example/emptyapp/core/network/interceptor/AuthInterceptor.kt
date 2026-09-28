package com.example.emptyapp.core.network.interceptor

import com.example.emptyapp.core.network.token.TokenProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Adds `Authorization: Bearer <token>` to outgoing requests when a token is
 * available. Auth endpoints (login/register) are skipped so the header never
 * leaks onto unauthenticated calls.
 */
class AuthInterceptor(
    private val tokenProvider: TokenProvider,
    private val authEndpointPaths: Set<String> = DEFAULT_AUTH_ENDPOINTS,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.isAuthEndpoint()) return chain.proceed(request)

        val token = tokenProvider.currentAccessToken()
        if (token.isNullOrBlank()) return chain.proceed(request)

        val authorized = request.newBuilder()
            .header(HEADER_AUTHORIZATION, "$BEARER_PREFIX$token")
            .build()
        return chain.proceed(authorized)
    }

    private fun Request.isAuthEndpoint(): Boolean =
        authEndpointPaths.any { url.encodedPath.contains(it, ignoreCase = true) }

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val BEARER_PREFIX = "Bearer "

        val DEFAULT_AUTH_ENDPOINTS = setOf("/auth/login", "/auth/register")
    }
}
