package com.example.emptyapp.feature.auth.presentation

import com.example.emptyapp.core.common.mvi.UiEffect
import com.example.emptyapp.core.common.mvi.UiEvent
import com.example.emptyapp.core.common.mvi.UiState
import com.example.emptyapp.core.designsystem.text.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val generalError: UiText? = null,
) : UiState {
    val isSubmitEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isSubmitting
}

sealed interface LoginEvent : UiEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object Submit : LoginEvent
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToGpsList : LoginEffect
}
