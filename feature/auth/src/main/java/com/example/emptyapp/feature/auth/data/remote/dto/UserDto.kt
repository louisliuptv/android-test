package com.example.emptyapp.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val email: String,
    val name: String,
    val phoneNumber: String,
)
