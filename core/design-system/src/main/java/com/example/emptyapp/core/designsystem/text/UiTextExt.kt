package com.example.emptyapp.core.designsystem.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** Resolves a [UiText] to a concrete string inside a composable. */
@Composable
fun UiText.asString(): String = when (this) {
    is UiText.DynamicString -> value
    is UiText.StringResource -> stringResource(resId, *args.toTypedArray())
}
