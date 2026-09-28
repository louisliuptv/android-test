package com.example.emptyapp.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
)
