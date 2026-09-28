package com.example.emptyapp.feature.gps.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.emptyapp.core.common.ui.UiText

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.DynamicString -> value
    is UiText.StringResource -> stringResource(resId, *args.toTypedArray())
}
