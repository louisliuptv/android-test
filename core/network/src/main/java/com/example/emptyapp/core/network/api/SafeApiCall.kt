package com.example.emptyapp.core.network.api

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.network.error.NetworkErrorMapper
import kotlinx.coroutines.CancellationException

/**
 * Runs [block] and wraps the outcome in an [ApiResult], mapping any thrown
 * exception to an [ApiError] via [NetworkErrorMapper]. Cancellation is never
 * swallowed.
 *
 * Usage: `safeApiCall { gpsApi.getGpsList(page) }`
 */
suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> =
    try {
        ApiResult.Success(block())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        ApiResult.Failure(NetworkErrorMapper.map(throwable))
    }
