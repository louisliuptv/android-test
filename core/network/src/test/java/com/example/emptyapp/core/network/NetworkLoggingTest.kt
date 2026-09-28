package com.example.emptyapp.core.network

import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkLoggingTest {

    @Test
    fun `debug builds log the full body`() {
        assertEquals(HttpLoggingInterceptor.Level.BODY, loggingLevelFor(isDebug = true))
    }

    @Test
    fun `release builds disable logging`() {
        assertEquals(HttpLoggingInterceptor.Level.NONE, loggingLevelFor(isDebug = false))
    }
}
