package com.example.emptyapp.feature.auth.presentation

import app.cash.turbine.test
import com.example.emptyapp.core.common.result.ApiError
import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.core.designsystem.text.ErrorMessageProvider
import com.example.emptyapp.core.designsystem.text.UiText
import com.example.emptyapp.feature.auth.MainDispatcherRule
import com.example.emptyapp.feature.auth.R
import com.example.emptyapp.feature.auth.domain.repository.AuthRepository
import com.example.emptyapp.feature.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAuthRepository()
    private val viewModel = LoginViewModel(
        loginUseCase = LoginUseCase(repository),
        errorMessageProvider = ErrorMessageProvider { error ->
            UiText.DynamicString("mapped:${error.javaClass.simpleName}")
        },
    )

    @Test
    fun `starts in the initial state`() {
        assertEquals(LoginState(), viewModel.uiState.value)
    }

    @Test
    fun `submit with empty fields shows validation errors`() = runTest {
        viewModel.onEvent(LoginEvent.Submit)

        val state = viewModel.uiState.value
        assertEquals(UiText.StringResource(R.string.login_error_email_required), state.emailError)
        assertEquals(UiText.StringResource(R.string.login_error_password_required), state.passwordError)
        assertEquals(0, repository.loginCalls)
    }

    @Test
    fun `editing a field clears its validation error`() = runTest {
        viewModel.onEvent(LoginEvent.Submit)

        viewModel.onEvent(LoginEvent.EmailChanged("alice@example.com"))
        assertNull(viewModel.uiState.value.emailError)

        viewModel.onEvent(LoginEvent.PasswordChanged("secret"))
        assertNull(viewModel.uiState.value.passwordError)
    }

    @Test
    fun `submit trims the email and calls the repository`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("  alice@example.com  "))
        viewModel.onEvent(LoginEvent.PasswordChanged("secret"))

        viewModel.onEvent(LoginEvent.Submit)

        assertEquals(1, repository.loginCalls)
        assertEquals("alice@example.com", repository.lastEmail)
    }

    @Test
    fun `successful login emits navigation and stops loading`() = runTest {
        viewModel.onEvent(LoginEvent.EmailChanged("alice@example.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("secret"))

        viewModel.effects.test {
            viewModel.onEvent(LoginEvent.Submit)
            assertEquals(LoginEffect.NavigateToGpsList, awaitItem())
        }
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertNull(viewModel.uiState.value.generalError)
    }

    @Test
    fun `failed login shows the mapped error and stops loading`() = runTest {
        repository.loginResult = ApiResult.Failure(ApiError.Unauthorized)
        viewModel.onEvent(LoginEvent.EmailChanged("alice@example.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("wrong"))

        viewModel.onEvent(LoginEvent.Submit)

        val state = viewModel.uiState.value
        assertEquals(UiText.DynamicString("mapped:Unauthorized"), state.generalError)
        assertFalse(state.isSubmitting)
    }

    private class FakeAuthRepository : AuthRepository {
        var loginResult: ApiResult<Unit> = ApiResult.Success(Unit)
        var loginCalls = 0
        var lastEmail: String? = null

        override suspend fun login(email: String, password: String): ApiResult<Unit> {
            loginCalls++
            lastEmail = email
            return loginResult
        }

        override suspend fun logout() = Unit

        override suspend fun isLoggedIn(): Boolean = false
    }
}
