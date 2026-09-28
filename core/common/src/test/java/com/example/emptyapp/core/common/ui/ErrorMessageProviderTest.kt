package com.example.emptyapp.core.common.ui

import com.example.emptyapp.core.common.R
import com.example.emptyapp.core.common.result.ApiError
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorMessageProviderTest {

    @Test
    fun `connectivity errors map to static resources`() {
        assertEquals(
            UiText.StringResource(R.string.error_no_network),
            DefaultErrorMessageProvider.getMessage(ApiError.NoNetwork),
        )
        assertEquals(
            UiText.StringResource(R.string.error_timeout),
            DefaultErrorMessageProvider.getMessage(ApiError.Timeout),
        )
    }

    @Test
    fun `auth errors map to static resources`() {
        assertEquals(
            UiText.StringResource(R.string.error_unauthorized),
            DefaultErrorMessageProvider.getMessage(ApiError.Unauthorized),
        )
        assertEquals(
            UiText.StringResource(R.string.error_forbidden),
            DefaultErrorMessageProvider.getMessage(ApiError.Forbidden),
        )
    }

    @Test
    fun `server error prefers the server message when present`() {
        assertEquals(
            UiText.DynamicString("Service unavailable"),
            DefaultErrorMessageProvider.getMessage(ApiError.Server(503, "Service unavailable")),
        )
        assertEquals(
            UiText.StringResource(R.string.error_server),
            DefaultErrorMessageProvider.getMessage(ApiError.Server(503)),
        )
    }

    @Test
    fun `blank server message falls back to the resource`() {
        assertEquals(
            UiText.StringResource(R.string.error_bad_request),
            DefaultErrorMessageProvider.getMessage(ApiError.BadRequest("  ")),
        )
    }

    @Test
    fun `toUiText uses the supplied provider`() {
        val custom = ErrorMessageProvider { UiText.DynamicString("custom") }

        assertEquals(UiText.DynamicString("custom"), ApiError.NotFound().toUiText(custom))
    }

    @Test
    fun `toUiText defaults to the built-in provider`() {
        assertEquals(
            UiText.StringResource(R.string.error_serialization),
            ApiError.Serialization().toUiText(),
        )
    }
}
