package com.example.emptyapp.feature.auth.domain.usecase

import com.example.emptyapp.core.common.result.ApiResult
import com.example.emptyapp.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): ApiResult<Unit> =
        repository.login(email = email, password = password)
}
