package com.example.emptyapp.core.network.token

/**
 * Authentication state of the current session. Owned by `:core:network` and
 * consumed by the interceptors / authenticator.
 */
sealed interface TokenStatus {
    data object Authenticated : TokenStatus
    data object Unauthenticated : TokenStatus
}
