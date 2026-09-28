package com.example.emptyapp.core.network.token

import kotlinx.coroutines.flow.StateFlow

/**
 * Owns the session lifecycle. The app observes [sessionState] to route between
 * the authenticated area and login.
 */
interface SessionManager {
    val sessionState: StateFlow<TokenStatus>
}
