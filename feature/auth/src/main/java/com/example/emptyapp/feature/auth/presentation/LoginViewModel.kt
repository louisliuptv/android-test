package com.example.emptyapp.feature.auth.presentation

import com.example.emptyapp.core.common.mvi.BaseMviViewModel
import com.example.emptyapp.core.common.result.onFailure
import com.example.emptyapp.core.common.result.onSuccess
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import com.example.emptyapp.core.designsystem.text.UiText
import com.example.emptyapp.feature.auth.R
import com.example.emptyapp.feature.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val errorMessageProvider: ErrorMessageProvider,
) : BaseMviViewModel<LoginState, LoginEvent, LoginEffect>(LoginState()) {

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                setState { copy(email = event.value, emailError = null, generalError = null) }

            is LoginEvent.PasswordChanged ->
                setState { copy(password = event.value, passwordError = null, generalError = null) }

            LoginEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = currentState
        val emailError =
            if (state.email.isBlank()) UiText.StringResource(R.string.login_error_email_required) else null
        val passwordError =
            if (state.password.isBlank()) UiText.StringResource(R.string.login_error_password_required) else null

        if (emailError != null || passwordError != null) {
            setState { copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        setState { copy(isSubmitting = true, generalError = null) }
        launch {
            loginUseCase(state.email.trim(), state.password)
                .onSuccess {
                    setState { copy(isSubmitting = false) }
                    sendEffect(LoginEffect.NavigateToGpsList)
                }
                .onFailure { error ->
                    setState {
                        copy(
                            isSubmitting = false,
                            generalError = errorMessageProvider.getMessage(error),
                        )
                    }
                }
        }
    }
}
