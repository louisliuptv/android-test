package com.example.emptyapp.core.network.token

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Temporary [SessionManager] used until `:feature:auth` (Phase 5) provides the
 * real session store. Keeps the Hilt graph complete in the meantime.
 */
class PlaceholderSessionManager @Inject constructor() : SessionManager {

    private val _sessionState = MutableStateFlow<TokenStatus>(TokenStatus.Unauthenticated)
    override val sessionState: StateFlow<TokenStatus> = _sessionState.asStateFlow()

    override fun onSessionExpired() {
        _sessionState.value = TokenStatus.Unauthenticated
    }
}
