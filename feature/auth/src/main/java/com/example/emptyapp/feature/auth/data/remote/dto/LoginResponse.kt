package com.example.emptyapp.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto,
)
