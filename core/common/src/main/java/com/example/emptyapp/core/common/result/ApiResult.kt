package com.example.emptyapp.core.common.result

/**
 * Result of an operation that can fail with an [ApiError].
 *
 * `Success` carries the data, `Failure` carries the mapped error. This keeps
 * exceptions out of the domain and presenter layers.
 */
sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Failure(val error: ApiError) : ApiResult<Nothing>
}

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Failure -> this
}

inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) action(data)
    return this
}

inline fun <T> ApiResult<T>.onFailure(action: (ApiError) -> Unit): ApiResult<T> {
    if (this is ApiResult.Failure) action(error)
    return this
}

inline fun <T, R> ApiResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (ApiError) -> R,
): R = when (this) {
    is ApiResult.Success -> onSuccess(data)
    is ApiResult.Failure -> onFailure(error)
}

fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data

fun <T> ApiResult<T>.getOrElse(fallback: (ApiError) -> T): T = when (this) {
    is ApiResult.Success -> data
    is ApiResult.Failure -> fallback(error)
}

fun <T> ApiResult<T>.errorOrNull(): ApiError? = (this as? ApiResult.Failure)?.error
