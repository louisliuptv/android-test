package com.example.emptyapp.core.network

import okhttp3.logging.HttpLoggingInterceptor

/**
 * Logging is verbose in debug builds and fully disabled in release builds.
 * Extracted so the choice is unit-testable without [BuildConfig].
 */
internal fun loggingLevelFor(isDebug: Boolean): HttpLoggingInterceptor.Level =
    if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
