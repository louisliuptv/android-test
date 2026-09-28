package com.example.emptyapp.feature.auth.data.session

import com.example.emptyapp.core.network.token.SessionManager
import com.example.emptyapp.core.network.token.TokenStatus
import com.example.emptyapp.feature.auth.data.token.TokenStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Session state backed by the secure [TokenStore]. Restores any persisted token
 * on creation so a returning user stays signed in.
 */
@Singleton
class SessionManagerImpl @Inject constructor(
    private val tokenStore: TokenStore,
) : SessionManager {

    private val _sessionState = MutableStateFlow<TokenStatus>(TokenStatus.Unauthenticated)
    override val sessionState: StateFlow<TokenStatus> = _sessionState.asStateFlow()

    init {
        restore()
    }

    fun onAuthenticated(accessToken: String) {
        tokenStore.save(accessToken)
        _sessionState.value = TokenStatus.Authenticated
    }

    fun onLoggedOut() {
        tokenStore.clear()
        _sessionState.value = TokenStatus.Unauthenticated
    }

    fun isAuthenticated(): Boolean = _sessionState.value == TokenStatus.Authenticated

    private fun restore() {
        val token = tokenStore.restore()
        _sessionState.value =
            if (token.isNullOrBlank()) TokenStatus.Unauthenticated else TokenStatus.Authenticated
    }
}
