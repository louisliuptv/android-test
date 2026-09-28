package com.example.emptyapp.feature.auth.data.token

import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory, fast-access copy of the access token. The single source of truth
 * during a session; [TokenStore] keeps it in sync with the keystore.
 */
@Singleton
class InMemoryTokenCache @Inject constructor() {

    @Volatile
    private var accessToken: String? = null

    fun currentAccessToken(): String? = accessToken

    fun update(token: String?) {
        accessToken = token
    }
}
