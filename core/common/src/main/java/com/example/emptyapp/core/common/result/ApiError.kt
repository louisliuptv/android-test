package com.example.emptyapp.core.common.result

/**
 * Domain-agnostic error representation. Network and data layers map their
 * platform-specific failures (exceptions, HTTP codes) onto this type so that
 * the UI never has to deal with them directly.
 */
sealed interface ApiError {
    data object NoNetwork : ApiError
    data object Timeout : ApiError
    data object Unauthorized : ApiError
    data object Forbidden : ApiError
    data class BadRequest(val message: String? = null) : ApiError
    data class NotFound(val message: String? = null) : ApiError
    data class Server(val code: Int, val message: String? = null) : ApiError
    data class Serialization(val message: String? = null) : ApiError
    data class Unknown(
        val message: String? = null,
        val cause: Throwable? = null,
    ) : ApiError
}
