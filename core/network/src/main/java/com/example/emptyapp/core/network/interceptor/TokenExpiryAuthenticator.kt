package com.example.emptyapp.core.network.interceptor

import com.example.emptyapp.core.network.token.SessionManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Handles `401 Unauthorized` responses. There is no refresh flow: the session
 * is expired and the request is not retried, so the original 401 reaches the
 * caller and the UI can route back to login.
 */
class TokenExpiryAuthenticator(
    private val sessionManager: SessionManager,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == HTTP_UNAUTHORIZED) {
            sessionManager.onSessionExpired()
        }
        return null
    }

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
    }
}
