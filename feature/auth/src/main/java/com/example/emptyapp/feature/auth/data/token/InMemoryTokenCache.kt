package com.example.emptyapp.feature.auth.data.token

import com.example.emptyapp.core.network.token.TokenProvider
import com.example.emptyapp.core.network.token.TokenStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory, fast-access copy of the access token. Implements the network
 * [TokenProvider] contract and is the single source during a session.
 */
@Singleton
class InMemoryTokenCache @Inject constructor() : TokenProvider {

    @Volatile
    private var accessToken: String? = null

    override fun currentAccessToken(): String? = accessToken

    override fun currentStatus(): TokenStatus =
        if (accessToken.isNullOrBlank()) TokenStatus.Unauthenticated else TokenStatus.Authenticated

    fun update(token: String?) {
        accessToken = token
    }
}
