package com.example.emptyapp.core.network.token

/**
 * Read access to the current access token. A missing/blank token simply means
 * the caller is not signed in; there is no separate auth state to query.
 */
interface TokenProvider {
    fun currentAccessToken(): String?
}
