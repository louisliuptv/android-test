package com.example.emptyapp.core.network.token

import kotlinx.coroutines.flow.StateFlow

/**
 * Owns the session lifecycle. When a request comes back `401`, the network
 * layer calls [onSessionExpired]; the app observes [sessionState] to route back
 * to login. There is no token refresh flow.
 */
interface SessionManager {
    val sessionState: StateFlow<TokenStatus>
    fun onSessionExpired()
}
