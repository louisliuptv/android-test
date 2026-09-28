package com.example.emptyapp.core.network.token

import javax.inject.Inject

/**
 * Temporary [TokenProvider] used until `:feature:auth` (Phase 5) provides the
 * secure implementation. Keeps the Hilt graph complete in the meantime.
 */
class PlaceholderTokenProvider @Inject constructor() : TokenProvider {

    @Volatile
    private var accessToken: String? = null

    override fun currentAccessToken(): String? = accessToken

    override fun currentStatus(): TokenStatus =
        if (accessToken.isNullOrBlank()) TokenStatus.Unauthenticated else TokenStatus.Authenticated
}
