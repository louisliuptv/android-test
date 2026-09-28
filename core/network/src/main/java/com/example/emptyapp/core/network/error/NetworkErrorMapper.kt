package com.example.emptyapp.core.network.error

import com.example.emptyapp.core.common.result.ApiError
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

/**
 * Single place that translates platform failures (exceptions, HTTP codes) into
 * the shared [ApiError]. Used by [com.example.emptyapp.core.network.api.safeApiCall]
 * and by repositories.
 */
object NetworkErrorMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun map(throwable: Throwable): ApiError = when (throwable) {
        is HttpException -> mapHttp(throwable)
        is UnknownHostException, is ConnectException, is NoRouteToHostException -> ApiError.NoNetwork
        is SocketTimeoutException, is InterruptedIOException -> ApiError.Timeout
        is SerializationException -> ApiError.Serialization(throwable.message)
        else -> ApiError.Unknown(throwable.message, throwable)
    }

    fun mapHttp(exception: HttpException): ApiError {
        val code = exception.code()
        val message = exception.serverMessage()
        return when (code) {
            400 -> ApiError.BadRequest(message)
            401 -> ApiError.Unauthorized
            403 -> ApiError.Forbidden
            404 -> ApiError.NotFound(message)
            in 500..599 -> ApiError.Server(code, message)
            else -> ApiError.Unknown(message ?: exception.message(), exception)
        }
    }

    private fun HttpException.serverMessage(): String? {
        val raw = runCatching { response()?.errorBody()?.string() }.getOrNull()
        if (raw.isNullOrBlank()) return null
        val parsed = runCatching {
            json.decodeFromString<ErrorEnvelope>(raw).message
        }.getOrNull()
        return parsed?.takeIf { it.isNotBlank() } ?: raw.take(MAX_BODY_LENGTH)
    }

    private const val MAX_BODY_LENGTH = 200
}

@Serializable
private data class ErrorEnvelope(val message: String? = null)
