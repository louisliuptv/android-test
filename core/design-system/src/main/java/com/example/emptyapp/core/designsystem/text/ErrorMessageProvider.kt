package com.example.emptyapp.core.designsystem.text

import androidx.annotation.StringRes
import com.example.emptyapp.core.common.result.ApiError
import com.example.emptyapp.core.designsystem.R

/**
 * Maps an [ApiError] to user-facing [UiText]. Implement this when a screen
 * needs custom copy, otherwise use [DefaultErrorMessageProvider].
 */
fun interface ErrorMessageProvider {
    fun getMessage(error: ApiError): UiText
}

object DefaultErrorMessageProvider : ErrorMessageProvider {
    override fun getMessage(error: ApiError): UiText = when (error) {
        ApiError.NoNetwork -> UiText.StringResource(R.string.error_no_network)
        ApiError.Timeout -> UiText.StringResource(R.string.error_timeout)
        ApiError.Unauthorized -> UiText.StringResource(R.string.error_unauthorized)
        ApiError.Forbidden -> UiText.StringResource(R.string.error_forbidden)
        is ApiError.BadRequest -> error.message.toUiTextOr(R.string.error_bad_request)
        is ApiError.NotFound -> error.message.toUiTextOr(R.string.error_not_found)
        is ApiError.Server -> error.message.toUiTextOr(R.string.error_server)
        is ApiError.Serialization -> UiText.StringResource(R.string.error_serialization)
        is ApiError.Unknown -> UiText.StringResource(R.string.error_unknown)
    }

    private fun String?.toUiTextOr(@StringRes fallback: Int): UiText =
        if (isNullOrBlank()) UiText.StringResource(fallback) else UiText.DynamicString(this)
}

fun ApiError.toUiText(provider: ErrorMessageProvider = DefaultErrorMessageProvider): UiText =
    provider.getMessage(this)
