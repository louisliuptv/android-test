package com.example.emptyapp.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the common headers every request should carry.
 */
class HeaderInterceptor(
    private val userAgent: String = DEFAULT_USER_AGENT,
    private val appVersion: String = DEFAULT_APP_VERSION,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(HEADER_ACCEPT, CONTENT_TYPE_JSON)
            .header(HEADER_USER_AGENT, userAgent)
            .header(HEADER_APP_VERSION, appVersion)
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val HEADER_ACCEPT = "Accept"
        const val HEADER_USER_AGENT = "User-Agent"
        const val HEADER_APP_VERSION = "X-App-Version"
        const val CONTENT_TYPE_JSON = "application/json"
        const val DEFAULT_USER_AGENT = "EmptyApp-Android"
        const val DEFAULT_APP_VERSION = "1.0"
    }
}
