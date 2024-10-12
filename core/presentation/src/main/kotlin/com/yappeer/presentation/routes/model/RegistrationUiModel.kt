package com.yappeer.presentation.routes.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationUiModel(
    val id: String,
    val username: String,
    val email: String,
)
