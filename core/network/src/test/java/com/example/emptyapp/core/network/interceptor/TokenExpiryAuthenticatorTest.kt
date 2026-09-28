package com.example.emptyapp.core.network.interceptor

import com.example.emptyapp.core.network.token.SessionManager
import com.example.emptyapp.core.network.token.TokenStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TokenExpiryAuthenticatorTest {

    private lateinit var server: MockWebServer
    private val sessionManager = FakeSessionManager()

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `a 401 expires the session and does not retry`() {
        server.enqueue(MockResponse().setResponseCode(401))
        val client = OkHttpClient.Builder()
            .authenticator(TokenExpiryAuthenticator(sessionManager))
            .build()

        val response = client
            .newCall(Request.Builder().url(server.url("/gps")).build())
            .execute()

        assertEquals(401, response.code)
        assertTrue(sessionManager.expired)
        assertEquals(TokenStatus.Unauthenticated, sessionManager.sessionState.value)
        assertEquals(1, server.requestCount)
    }

    @Test
    fun `a successful response leaves the session untouched`() {
        server.enqueue(MockResponse().setResponseCode(200))
        val client = OkHttpClient.Builder()
            .authenticator(TokenExpiryAuthenticator(sessionManager))
            .build()

        client.newCall(Request.Builder().url(server.url("/gps")).build()).execute().close()

        assertEquals(false, sessionManager.expired)
    }

    private class FakeSessionManager : SessionManager {
        private val state = MutableStateFlow<TokenStatus>(TokenStatus.Authenticated)
        override val sessionState: StateFlow<TokenStatus> = state.asStateFlow()
        var expired = false

        override fun onSessionExpired() {
            expired = true
            state.value = TokenStatus.Unauthenticated
        }
    }
}
