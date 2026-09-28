package com.example.emptyapp.core.designsystem.text

import androidx.annotation.StringRes

/**
 * Text that is resolved to a string only at the presentation layer, so that
 * ViewModels never touch `Context` or `Resources`.
 */
sealed interface UiText {
    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList(),
    ) : UiText

    data class DynamicString(val value: String) : UiText
}
