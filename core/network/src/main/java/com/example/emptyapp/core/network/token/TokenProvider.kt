package com.example.emptyapp.core.network.token

/**
 * Read access to the current access token. Implemented by `:feature:auth` in
 * Phase 5 (Keystore + encrypted storage + in-memory cache).
 */
interface TokenProvider {
    fun currentAccessToken(): String?
    fun currentStatus(): TokenStatus
}
