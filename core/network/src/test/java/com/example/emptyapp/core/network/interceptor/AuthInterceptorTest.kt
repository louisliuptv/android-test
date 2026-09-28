package com.example.emptyapp.core.network.interceptor

import com.example.emptyapp.core.network.token.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private lateinit var server: MockWebServer

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
    fun `adds a bearer header when a token is present`() {
        server.enqueue(MockResponse().setResponseCode(200))
        val client = clientWith(AuthInterceptor(FakeTokenProvider("token-123")))

        client.newCall(Request.Builder().url(server.url("/gps")).build()).execute().close()

        assertEquals("Bearer token-123", server.takeRequest().getHeader("Authorization"))
    }

    @Test
    fun `omits the header when no token is available`() {
        server.enqueue(MockResponse().setResponseCode(200))
        val client = clientWith(AuthInterceptor(FakeTokenProvider(null)))

        client.newCall(Request.Builder().url(server.url("/gps")).build()).execute().close()

        assertNull(server.takeRequest().getHeader("Authorization"))
    }

    @Test
    fun `skips auth endpoints`() {
        server.enqueue(MockResponse().setResponseCode(200))
        val client = clientWith(AuthInterceptor(FakeTokenProvider("token-123")))

        client.newCall(Request.Builder().url(server.url("/auth/login")).build()).execute().close()

        assertNull(server.takeRequest().getHeader("Authorization"))
    }

    private fun clientWith(interceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    private class FakeTokenProvider(private val token: String?) : TokenProvider {
        override fun currentAccessToken(): String? = token
    }
}
